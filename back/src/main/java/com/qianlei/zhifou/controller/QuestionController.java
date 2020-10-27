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
  public QuestionDetailVo getQuestionById(
      @PathVariable Integer id,
      @RequestParam(value = "sort_direction", defaultValue = "asc") String sortDirection,
      @RequestParam(value = "sort_by", defaultValue = "createTime") String sortBy,
      @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
    return questionService.getQuestionById(id, sortBy,sortDirection, pageNum, pageSize);
  }

  @GetMapping("/random")
  public List<Question> getRandomQuestion(@RequestParam(defaultValue = "10") int num) {
    return questionService.getRandomQuestion(num);
  }
}
