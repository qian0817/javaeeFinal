package com.qianlei.zhifou.service.impl;

import com.qianlei.zhifou.common.BaseResponse;
import com.qianlei.zhifou.dao.QuestionDao;
import com.qianlei.zhifou.entity.Question;
import com.qianlei.zhifou.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author qianlei
 */
@Service
public class QuestionServiceImpl implements IQuestionService {
  @Autowired
  private QuestionDao questionDao;

  @Override
  public Mono<BaseResponse<Question>> createQuestion(Question question) {
    question.setId(null);
    return questionDao.save(question).map(BaseResponse::new);
  }
}
