package com.qianlei.zhifou.service.impl;

import cn.hutool.http.HtmlUtil;
import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.AnswerDao;
import com.qianlei.zhifou.dao.QuestionDao;
import com.qianlei.zhifou.dao.es.QuestionElasticsearchDao;
import com.qianlei.zhifou.pojo.Question;
import com.qianlei.zhifou.pojo.es.QuestionEs;
import com.qianlei.zhifou.requestparam.CreateQuestionParam;
import com.qianlei.zhifou.service.IQuestionService;
import com.qianlei.zhifou.utils.HtmlUtils;
import com.qianlei.zhifou.vo.QuestionHotVo;
import com.qianlei.zhifou.vo.QuestionVo;
import com.qianlei.zhifou.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import static com.qianlei.zhifou.common.Constant.HotQuestionConstant.HOT_QUESTION_TIME_FORMATTER;
import static com.qianlei.zhifou.common.Constant.RedisConstant.HOT_QUESTION_REDIS_PREFIX;

/** @author qianlei */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class QuestionServiceImpl implements IQuestionService {
  @Resource private QuestionElasticsearchDao questionElasticsearchDao;
  @Resource private AnswerDao answerDao;
  @Resource private QuestionDao questionDao;
  @Resource private StringRedisTemplate stringRedisTemplate;

  @Override
  public Question createQuestion(CreateQuestionParam param) {
    // XSS 过滤
    param.setContent(HtmlUtils.cleanHtmlRelaxed(param.getContent()));
    if (StringUtils.isBlank(HtmlUtil.cleanHtmlTag(param.getContent()))) {
      throw new ZhiFouException("问题内容不能为空");
    }
    if (StringUtils.isBlank(param.getTitle())) {
      throw new ZhiFouException("标题不能为空");
    }
    var question = param.toQuestion();
    questionDao.save(question);
    return question;
  }

  @Override
  public Question getQuestionById(Integer questionId) {
    return questionDao.findById(questionId).orElseThrow(() -> new ZhiFouException("问题id不存在"));
  }

  @Override
  public void improveQuestionHeatLevel(Integer questionId, Long number) {
    // 设置每小时的热榜
    var currentHour = LocalDateTime.now().format(HOT_QUESTION_TIME_FORMATTER);
    improveQuestionHeatLevel(questionId, number, currentHour);
  }

  @Override
  public void improveQuestionHeatLevel(Integer questionId, Long number, String time) {
    if (number <= 0) {
      return;
    }
    // 检查格式是否符合要求
    LocalDateTime.parse(time, HOT_QUESTION_TIME_FORMATTER);
    stringRedisTemplate
        .boundZSetOps(HOT_QUESTION_REDIS_PREFIX + time)
        .incrementScore(questionId.toString(), number);
  }

  @Override
  public Page<Question> searchQuestion(String keyword, int pageNum, int pageSize) {
    return questionElasticsearchDao
        .findAllByTitleContaining(keyword, PageRequest.of(pageNum, pageSize))
        .map(QuestionEs::getId)
        .map(id -> questionDao.findById(id).orElse(null));
  }

  @Override
  public QuestionVo getQuestionVoById(Integer id, UserVo user) {
    var question = getQuestionById(id);
    improveQuestionHeatLevel(id, 1L);
    if (user == null) {
      return new QuestionVo(question, true, null);
    }
    var myAnswer = answerDao.findByUserIdAndQuestionId(user.getId(), id);
    if (myAnswer == null) {
      return new QuestionVo(question, true, null);
    } else {
      return new QuestionVo(question, false, myAnswer.getId());
    }
  }

  @Override
  public List<QuestionHotVo> getHottestQuestion(int num) {
    var currentHour = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd:HH"));
    // 从 redis 之中获取热榜的数据
    var questionIdList =
        stringRedisTemplate
            .boundZSetOps(HOT_QUESTION_REDIS_PREFIX + currentHour)
            .reverseRangeWithScores(0, num - 1);
    if (questionIdList == null) {
      return new ArrayList<>();
    }
    return questionIdList.stream()
        .map(
            tuple -> {
              if (tuple.getScore() == null || tuple.getValue() == null) {
                return null;
              }
              var questionId = Integer.valueOf(tuple.getValue());
              var score = tuple.getScore().longValue();
              var question = questionDao.findById(questionId).orElseThrow();
              return new QuestionHotVo(question, score);
            })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  @Override
  public List<Question> getRandomQuestion(int num) {
    if (num < 1) {
      num = 1;
    }
    var totalQuestion = questionDao.count();
    if (totalQuestion == 0) {
      return new ArrayList<>();
    }
    return new Random()
        .longs(num, 0, totalQuestion)
        .mapToObj(questionDao::findOne)
        .collect(Collectors.toList());
  }
}
