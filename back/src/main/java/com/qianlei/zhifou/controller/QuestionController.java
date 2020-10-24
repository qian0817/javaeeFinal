package com.qianlei.zhifou.controller;

import com.qianlei.zhifou.common.BaseResponse;
import com.qianlei.zhifou.entity.Question;
import com.qianlei.zhifou.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author qianlei
 */
@RestController
@RequestMapping("/api/question")
public class QuestionController {
  @Autowired
  private IQuestionService questionService;

  @PostMapping("/")
  public Mono<BaseResponse<Question>> createQuestion(@RequestBody Question question) {
    return questionService.createQuestion(question);
  }
}
