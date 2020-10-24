package com.qianlei.zhifou.service;

import com.qianlei.zhifou.common.BaseResponse;
import com.qianlei.zhifou.entity.Question;
import reactor.core.publisher.Mono;

/**
 * @author qianlei
 */
public interface IQuestionService {
  /**
   * 创建问题
   *
   * @param question 问题内容
   * @return 问题内容，添加id信息
   */
  Mono<BaseResponse<Question>> createQuestion(Question question);
}
