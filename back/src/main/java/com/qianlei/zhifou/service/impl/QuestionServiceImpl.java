package com.qianlei.zhifou.service.impl;

import cn.hutool.http.HtmlUtil;
import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.es.AnswerDao;
import com.qianlei.zhifou.dao.es.QuestionDao;
import com.qianlei.zhifou.pojo.es.Question;
import com.qianlei.zhifou.service.IQuestionService;
import com.qianlei.zhifou.vo.QuestionHotVo;
import com.qianlei.zhifou.vo.QuestionVo;
import com.qianlei.zhifou.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/** @author qianlei */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class QuestionServiceImpl implements IQuestionService {
  @Autowired private AnswerDao answerDao;
  @Autowired private QuestionDao questionDao;
  @Autowired private StringRedisTemplate redisTemplate;

  @Override
  public Question createQuestion(Question question) {
    // XSS 过滤
    question.setContent(Jsoup.clean(question.getContent(), Whitelist.relaxed()));
    if (StringUtils.isBlank(HtmlUtil.cleanHtmlTag(question.getContent()))) {
      throw new ZhiFouException("问题内容不能为空");
    }
    if (StringUtils.isBlank(question.getTitle())) {
      throw new ZhiFouException("标题不能为空");
    }
    question.setId(null);
    return questionDao.save(question);
  }

  @Override
  public Question getQuestionById(String questionId) {
    return questionDao.findById(questionId).orElseThrow(() -> new ZhiFouException("问题id不存在"));
  }

  @Override
  public void improveQuestionHeatLevel(String questionId, int number) {
    // 设置每小时的热榜
    var currentHour = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy MM dd HH"));
    // 将热榜的信息保存到 redis 之中。
    redisTemplate
        .boundZSetOps("zhifou:question:hot:" + currentHour)
        .incrementScore(questionId, number);
  }

  @Override
  public Page<Question> searchQuestion(String keyword, int pageNum, int pageSize) {
    return questionDao.findAllByTitleContaining(keyword, PageRequest.of(pageNum, pageSize));
  }

  @Override
  public QuestionVo getQuestionVoById(String id, UserVo user) {
    var question = getQuestionById(id);
    improveQuestionHeatLevel(id, 1);
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
  public List<QuestionHotVo> getHottestQuestion() {
    var currentHour = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy MM dd HH"));
    // 从 redis 之中获取热榜的数据
    var questionIdList =
        redisTemplate
            .boundZSetOps("zhifou:question:hot:" + currentHour)
            .reverseRangeWithScores(0, 29);
    if (questionIdList == null) {
      return new ArrayList<>();
    }
    return questionIdList.stream()
        .map(
            tuple -> {
              if (tuple.getScore() == null || tuple.getValue() == null) {
                return null;
              }
              var questionId = tuple.getValue();
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
    return questionDao.findRandomQuestion(PageRequest.of(0, num));
  }
}
