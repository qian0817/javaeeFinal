package com.qianlei.zhifou.service.impl;

import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.AgreeDao;
import com.qianlei.zhifou.dao.es.AnswerDao;
import com.qianlei.zhifou.dao.es.QuestionDao;
import com.qianlei.zhifou.pojo.Agree;
import com.qianlei.zhifou.pojo.User;
import com.qianlei.zhifou.pojo.es.Answer;
import com.qianlei.zhifou.service.IAnswerService;
import com.qianlei.zhifou.service.IQuestionService;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.vo.AnswerVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/** @author qianlei */
@Service
@Slf4j
@Transactional(rollbackFor = RuntimeException.class)
public class AnswerServiceImpl implements IAnswerService {
  @Autowired private AnswerDao answerDao;
  @Autowired private QuestionDao questionDao;
  @Autowired private StringRedisTemplate redisTemplate;
  @Autowired private IQuestionService questionService;
  @Autowired private IUserService userService;
  @Autowired private AgreeDao agreeDao;

  private static final List<String> SUPPORTED_SORT_BY_PROPERTIES =
      List.of("createTime", "updateTime");
  private static final List<String> SUPPORT_SORT_DIRECTION = List.of("asc", "desc");

  @Override
  public Answer createAnswer(Answer answer, User user) {
    if (StringUtils.isBlank(answer.getContent())) {
      throw new ZhiFouException("回答不能为空");
    }
    if (!questionDao.existsById(answer.getQuestionId())) {
      throw new ZhiFouException("问题不存在");
    }
    log.info("user{}", user);
    Long id = redisTemplate.boundValueOps("answerId").increment();
    answer.setId(id.intValue());
    answer.setUserId(user.getId());
    answer.setUpdateTime(LocalDateTime.now());
    answer.setCreateTime(LocalDateTime.now());
    answerDao.save(answer);
    questionService.improveQuestionHeatLevel(answer.getQuestionId(), 100);
    return answer;
  }

  @Override
  public AnswerVo getAnswerByQuestionId(int answerId, @Nullable User user) {
    var answer = answerDao.findById(answerId).orElseThrow(() -> new ZhiFouException("问题不存在"));
    // 回答者用户信息
    var answerUser = userService.getUserInfoByUserId(answer.getUserId());
    var question = questionDao.findById(answer.getQuestionId()).orElseThrow();
    long agreeNumber = agreeDao.countByAnswerId(answer.getId());
    questionService.improveQuestionHeatLevel(answer.getQuestionId(), 1);
    if (user == null) {
      return new AnswerVo(answer, answerUser, question, true, agreeNumber);
    } else {
      boolean canAgree = !agreeDao.existsByAnswerIdAndUserId(answerId, user.getId());
      return new AnswerVo(answer, answerUser, question, canAgree, agreeNumber);
    }
  }

  @Override
  public void agree(Integer answerId, User user) {
    if (!answerDao.existsById(answerId)) {
      throw new ZhiFouException("回答不存在");
    }
    if (agreeDao.existsByAnswerIdAndUserId(answerId, user.getId())) {
      throw new ZhiFouException("不能重复点赞");
    }
    agreeDao.save(new Agree(null, user.getId(), answerId));
  }

  @Override
  public void deleteAgree(Integer answerId, User user) {
    agreeDao.deleteByAnswerIdAndUserId(answerId, user.getId());
  }

  @Override
  public Page<AnswerVo> getAllAnswerByQuestionId(
      Integer questionId,
      String sortDirection,
      String sortBy,
      int pageNum,
      int pageSize,
      User user) {
    if (!SUPPORTED_SORT_BY_PROPERTIES.contains(sortBy)) {
      throw new ZhiFouException("不支持的排序类型" + sortBy);
    }
    if (!SUPPORT_SORT_DIRECTION.contains(sortDirection)) {
      throw new ZhiFouException("不支持的排序方向" + sortDirection);
    }
    return answerDao
        .findAllByQuestionId(
            questionId, PageRequest.of(pageNum, pageSize, Sort.by(Sort.Order.asc(sortBy))))
        .map(
            answer -> {
              var answerUser = userService.getUserInfoByUserId(answer.getUserId());
              var agreeNumber = agreeDao.countByAnswerId(answer.getId());
              if (user != null) {
                var canAgree = !agreeDao.existsByAnswerIdAndUserId(answer.getId(), user.getId());
                return new AnswerVo(answer, answerUser, null, canAgree, agreeNumber);
              } else {
                return new AnswerVo(answer, answerUser, null, true, agreeNumber);
              }
            });
  }
}
