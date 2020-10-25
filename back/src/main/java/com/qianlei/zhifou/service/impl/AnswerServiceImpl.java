package com.qianlei.zhifou.service.impl;

import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.AnswerDao;
import com.qianlei.zhifou.entity.Answer;
import com.qianlei.zhifou.service.IAnswerService;
import com.qianlei.zhifou.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/** @author qianlei */
@Service
public class AnswerServiceImpl implements IAnswerService {
  @Autowired private AnswerDao answerDao;
  @Autowired private IUserService userService;

  @Override
  public Answer createAnswer(Answer answer, String token) {
    var user = userService.getUserInfo(token);
    answer.setUserId(user.getId());
    answer.setCreateTime(LocalDateTime.now());
    answer.setUpdateTime(LocalDateTime.now());
    answerDao.save(answer);
    return answer;
  }

  @Override
  public Answer getAnswerById(int id) {
    return answerDao.findById(id).orElseThrow(() -> new ZhiFouException("不存在的问题id"));
  }
}
