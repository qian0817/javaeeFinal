package com.qianlei.zhifou.controller;

import com.qianlei.zhifou.pojo.es.Answer;
import com.qianlei.zhifou.service.IAnswerService;
import com.qianlei.zhifou.vo.AnswerVo;
import com.qianlei.zhifou.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** @author qianlei */
@RestController
@RequestMapping("/api/answer")
public class AnswerController {
  @Autowired private IAnswerService answerService;

  @GetMapping("/id/{id}")
  public AnswerVo getAnswerById(
      @PathVariable String id, @RequestAttribute(value = "user", required = false) UserVo user) {
    return answerService.getAnswerByQuestionId(id, user);
  }

  @PostMapping("/")
  public Answer createAnswer(
      @RequestBody Answer answer, @RequestAttribute(value = "user") UserVo user) {
    return answerService.createAnswer(answer, user);
  }

  @PostMapping("/id/{answerId}/agree/")
  public void agree(
      @PathVariable("answerId") String answerId, @RequestAttribute(value = "user") UserVo user) {
    answerService.agree(answerId, user);
  }

  @DeleteMapping("/id/{answerId}/agree/")
  public void removeAgree(
      @PathVariable("answerId") String answerId, @RequestAttribute(value = "user") UserVo user) {
    answerService.deleteAgree(answerId, user);
  }
}
