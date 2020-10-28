package com.qianlei.zhifou.service.impl;

import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.AgreeDao;
import com.qianlei.zhifou.dao.AnswerDao;
import com.qianlei.zhifou.dao.QuestionDao;
import com.qianlei.zhifou.entity.Agree;
import com.qianlei.zhifou.entity.Answer;
import com.qianlei.zhifou.service.IAnswerService;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.vo.AnswerVo;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** @author qianlei */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AnswerServiceImpl implements IAnswerService {
  @Autowired private AnswerDao answerDao;
  @Autowired private QuestionDao questionDao;
  @Autowired private IUserService userService;
  @Autowired private AgreeDao agreeDao;

  @Override
  public Answer createAnswer(Answer answer, String token) {
    var user = userService.getUserInfo(token);
    answer.setUserId(user.getId());
    answerDao.save(answer);
    return answer;
  }

  @Override
  public AnswerVo getAnswerById(int answerId, @Nullable String token) {
    var answer = answerDao.findById(answerId).orElseThrow(() -> new ZhiFouException("不存在的问题id"));
    // 回答者用户信息
    var answerUser = userService.getUserInfoByUserId(answer.getUserId());
    var question = questionDao.findById(answer.getQuestionId()).orElseThrow();
    long agreeNumber = agreeDao.countByAnswerId(answer.getId());
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
    var user = userService.getUserInfo(token);
    agreeDao.save(new Agree(null, user.getId(), answerId));
  }

  @Override
  public void deleteAgree(Integer answerId, String token) {
    var user = userService.getUserInfo(token);
    agreeDao.deleteByAnswerIdAndUserId(answerId, user.getId());
  }
}
