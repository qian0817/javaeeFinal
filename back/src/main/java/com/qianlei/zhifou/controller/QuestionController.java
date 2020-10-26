package com.qianlei.zhifou.controller;

import com.qianlei.zhifou.entity.Question;
import com.qianlei.zhifou.service.IQuestionService;
import com.qianlei.zhifou.vo.QuestionDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** @author qianlei */
@RestController
@RequestMapping("/api/question")
public class QuestionController {
  @Autowired private IQuestionService questionService;

  @PostMapping("/")
  public Question createQuestion(@RequestBody Question question) {
    return questionService.createQuestion(question);
  }

  @GetMapping("/id/{id}")
  public QuestionDetailVo getQuestionById(@PathVariable Integer id) {
    return questionService.getQuestionById(id);
  }

  @GetMapping("/random")
  public List<Question> getRandomQuestion(@RequestParam(defaultValue = "10") int num) {
    return questionService.getRandomQuestion(num);
  }
}
