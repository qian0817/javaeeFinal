package com.qianlei.zhifou.service;

import com.qianlei.zhifou.entity.Answer;
import com.qianlei.zhifou.vo.AnswerVo;
import org.jetbrains.annotations.Nullable;

/** @author qianlei */
public interface IAnswerService {
  /**
   * 根据 id 获取回答信息
   *
   * @param id 回答id
   * @param token 希望获取回答的用户 id
   * @return 回答信息
   */
  AnswerVo getAnswerById(int id, @Nullable String token);

  /**
   * 创建回答
   *
   * @param answer 回答内容
   * @param token 创建者 token
   * @return 创建的回答信息
   */
  Answer createAnswer(Answer answer, String token);

  /**
   * 赞同回答
   *
   * @param answerId 回答 id
   * @param token 赞同者 token
   */
  void agree(Integer answerId, String token);

  /**
   * 取消赞同回答
   *
   * @param answerId 回答 id
   * @param token 取消赞同者 token
   */
  void deleteAgree(Integer answerId, String token);
}
