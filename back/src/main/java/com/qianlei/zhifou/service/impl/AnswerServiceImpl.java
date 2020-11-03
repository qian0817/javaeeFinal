package com.qianlei.zhifou.service.impl;

import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.AgreeDao;
import com.qianlei.zhifou.dao.AnswerDao;
import com.qianlei.zhifou.dao.QuestionDao;
import com.qianlei.zhifou.entity.Agree;
import com.qianlei.zhifou.entity.Answer;
import com.qianlei.zhifou.service.IAnswerService;
import com.qianlei.zhifou.service.IQuestionService;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.vo.AnswerVo;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** @author qianlei */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AnswerServiceImpl implements IAnswerService {
  @Autowired private AnswerDao answerDao;
  @Autowired private QuestionDao questionDao;
  @Autowired private IQuestionService questionService;
  @Autowired private IUserService userService;
  @Autowired private AgreeDao agreeDao;

  private static final List<String> SUPPORTED_SORT_BY_PROPERTIES =
      List.of("createTime", "updateTime");
  private static final List<String> SUPPORT_SORT_DIRECTION = List.of("asc", "desc");

  @Override
  public Answer createAnswer(Answer answer, String token) {
    if (StringUtils.isBlank(answer.getContent())) {
      throw new ZhiFouException("回答不能为空");
    }
    if (!questionDao.existsById(answer.getQuestionId())) {
      throw new ZhiFouException("问题不存在");
    }
    var user = userService.getUserInfo(token);
    answer.setId(null);
    answer.setUserId(user.getId());
    answer.setUpdateTime(null);
    answer.setCreateTime(null);
    answerDao.save(answer);
    questionService.improveQuestionHeatLevel(answer.getQuestionId(), 100);
    return answer;
  }

  @Override
  public AnswerVo getAnswerByQuestionId(int answerId, @Nullable String token) {
    var answer = answerDao.findById(answerId).orElseThrow(() -> new ZhiFouException("不存在的问题id"));
    // 回答者用户信息
    var answerUser = userService.getUserInfoByUserId(answer.getUserId());
    var question = questionDao.findById(answer.getQuestionId()).orElseThrow();
    long agreeNumber = agreeDao.countByAnswerId(answer.getId());
    questionService.improveQuestionHeatLevel(answer.getQuestionId(), 1);
    if (token == null) {
      return new AnswerVo(answer, answerUser, question, true, agreeNumber);
    } else {
      var user = userService.getUserInfo(token);
      boolean canAgree = !agreeDao.existsByAnswerIdAndUserId(answerId, user.getId());
      return new AnswerVo(answer, answerUser, question, canAgree, agreeNumber);
    }
  }

  @Override
  public void agree(Integer answerId, String token) {
    if (!answerDao.existsById(answerId)) {
      throw new ZhiFouException("回答不存在");
    }
    var user = userService.getUserInfo(token);
    if (agreeDao.existsByAnswerIdAndUserId(answerId, user.getId())) {
      throw new ZhiFouException("不能重复点赞");
    }
    agreeDao.save(new Agree(null, user.getId(), answerId));
  }

  @Override
  public void deleteAgree(Integer answerId, String token) {
    var user = userService.getUserInfo(token);
    agreeDao.deleteByAnswerIdAndUserId(answerId, user.getId());
  }

  @Override
  public Page<AnswerVo> getAllAnswerByQuestionId(
      Integer questionId,
      String sortDirection,
      String sortBy,
      int pageNum,
      int pageSize,
      String token) {
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
              if (token != null) {
                var user = userService.getUserInfo(token);
                var canAgree = !agreeDao.existsByAnswerIdAndUserId(answer.getId(), user.getId());
                return new AnswerVo(answer, answerUser, null, canAgree, agreeNumber);
              } else {
                return new AnswerVo(answer, answerUser, null, true, agreeNumber);
              }
            });
  }
}
