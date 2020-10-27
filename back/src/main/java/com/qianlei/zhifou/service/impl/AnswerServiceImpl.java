package com.qianlei.zhifou.service.impl;

import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.AnswerDao;
import com.qianlei.zhifou.dao.QuestionDao;
import com.qianlei.zhifou.entity.Answer;
import com.qianlei.zhifou.service.IAnswerService;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.vo.AnswerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** @author qianlei */
@Service
public class AnswerServiceImpl implements IAnswerService {
  @Autowired private AnswerDao answerDao;
  @Autowired private QuestionDao questionDao;
  @Autowired private IUserService userService;

  @Override
  public Answer createAnswer(Answer answer, String token) {
    var user = userService.getUserInfo(token);
    answer.setUserId(user.getId());
    answerDao.save(answer);
    return answer;
  }

  @Override
  public AnswerVo getAnswerById(int id) {
    var answer = answerDao.findById(id).orElseThrow(() -> new ZhiFouException("不存在的问题id"));
    var user = userService.getUserInfoByUserId(answer.getUserId());
    var question = questionDao.findById(answer.getQuestionId()).orElseThrow();
    return new AnswerVo(answer, user, question);
  }
}
