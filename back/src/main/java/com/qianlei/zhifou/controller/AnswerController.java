package com.qianlei.zhifou.controller;

import com.qianlei.zhifou.entity.Answer;
import com.qianlei.zhifou.service.IAnswerService;
import com.qianlei.zhifou.vo.AnswerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** @author qianlei */
@RestController
@RequestMapping("/api/answer")
public class AnswerController {
  @Autowired public IAnswerService answerService;

  @GetMapping("/id/{id}")
  public AnswerVo getAnswerById(@PathVariable int id, @CookieValue(required = false) String token) {
    return answerService.getAnswerById(id, token);
  }

  @PostMapping("/")
  public Answer createAnswer(@RequestBody Answer answer, @CookieValue String token) {
    return answerService.createAnswer(answer, token);
  }

  @PostMapping("/id/{answerId}/agree/")
  public void agree(@PathVariable("answerId") Integer answerId, @CookieValue String token) {
    answerService.agree(answerId, token);
  }

  @DeleteMapping("/id/{answerId}/agree/")
  public void removeAgree(@PathVariable("answerId") Integer answerId, @CookieValue String token) {
    answerService.deleteAgree(answerId, token);
  }
}
