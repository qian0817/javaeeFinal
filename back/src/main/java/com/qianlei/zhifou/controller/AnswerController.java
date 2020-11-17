package com.qianlei.zhifou.controller;

import com.qianlei.zhifou.common.AuthorizationException;
import com.qianlei.zhifou.pojo.User;
import com.qianlei.zhifou.pojo.es.Answer;
import com.qianlei.zhifou.service.IAnswerService;
import com.qianlei.zhifou.vo.AnswerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/** @author qianlei */
@RestController
@RequestMapping("/api/answer")
public class AnswerController {
  @Autowired private IAnswerService answerService;

  @GetMapping("/id/{id}")
  public AnswerVo getAnswerById(@PathVariable String id, HttpSession session) {
    var user = (User) session.getAttribute("user");
    return answerService.getAnswerByQuestionId(id, user);
  }

  @PostMapping("/")
  public Answer createAnswer(@RequestBody Answer answer, HttpSession session) {
    var user = (User) session.getAttribute("user");
    if (user == null) {
      throw new AuthorizationException("用户未登录");
    }
    return answerService.createAnswer(answer, user);
  }

  @PostMapping("/id/{answerId}/agree/")
  public void agree(@PathVariable("answerId") String answerId, HttpSession session) {
    var user = (User) session.getAttribute("user");
    if (user == null) {
      throw new AuthorizationException("用户未登录");
    }
    answerService.agree(answerId, user);
  }

  @DeleteMapping("/id/{answerId}/agree/")
  public void removeAgree(@PathVariable("answerId") String answerId, HttpSession session) {
    var user = (User) session.getAttribute("user");
    if (user == null) {
      throw new AuthorizationException("用户未登录");
    }
    answerService.deleteAgree(answerId, user);
  }
}
