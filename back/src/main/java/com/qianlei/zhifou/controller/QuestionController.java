package com.qianlei.zhifou.controller;

import com.qianlei.zhifou.pojo.Question;
import com.qianlei.zhifou.pojo.User;
import com.qianlei.zhifou.service.IAnswerService;
import com.qianlei.zhifou.service.IQuestionService;
import com.qianlei.zhifou.vo.AnswerVo;
import com.qianlei.zhifou.vo.QuestionHotVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/** @author qianlei */
@RestController
@RequestMapping("/api/question")
public class QuestionController {
  @Autowired private IQuestionService questionService;
  @Autowired private IAnswerService answerService;

  @PostMapping("/")
  public Question createQuestion(@RequestBody Question question) {
    return questionService.createQuestion(question);
  }

  @GetMapping("/id/{id}")
  public Question getQuestionById(@PathVariable Integer id) {
    var question = questionService.getQuestionById(id);
    questionService.improveQuestionHeatLevel(id, 1);
    return question;
  }

  @GetMapping("/id/{id}/answers/")
  public Page<AnswerVo> getAnswerByQuestionId(
      @PathVariable("id") Integer questionId,
      @RequestParam(value = "sort_direction", defaultValue = "asc") String sortDirection,
      @RequestParam(value = "sort_by", defaultValue = "createTime") String sortBy,
      @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
      HttpSession session) {
    var user = (User) session.getAttribute("user");
    return answerService.getAllAnswerByQuestionId(
        questionId, sortDirection, sortBy, pageNum, pageSize, user);
  }

  @GetMapping("/random")
  public List<Question> getRandomQuestion(@RequestParam(defaultValue = "10") int num) {
    return questionService.getRandomQuestion(num);
  }

  @GetMapping("/hot/")
  public List<QuestionHotVo> getHottestQuestion() {
    return questionService.getHottestQuestion();
  }

  @GetMapping("/keyword/{keyword}")
  public Page<Question> searchQuestion(
      @PathVariable String keyword,
      @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
    return questionService.searchQuestion(keyword, pageNum, pageSize);
  }
}
