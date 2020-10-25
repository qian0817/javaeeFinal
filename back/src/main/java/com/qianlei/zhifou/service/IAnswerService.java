package com.qianlei.zhifou.service;

import com.qianlei.zhifou.entity.Answer;

/** @author qianlei */
public interface IAnswerService {
  Answer getAnswerById(int id);

  Answer createAnswer(Answer answer, String token);
}
