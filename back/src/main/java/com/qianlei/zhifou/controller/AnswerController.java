package com.qianlei.zhifou.controller;

import com.qianlei.zhifou.pojo.Answer;
import com.qianlei.zhifou.requestparam.CreateAnswerParam;
import com.qianlei.zhifou.service.IAnswerService;
import com.qianlei.zhifou.vo.AnswerVo;
import com.qianlei.zhifou.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** @author qianlei */
@RestController
@RequestMapping("/api/answer")
public class AnswerController {
  @Autowired private IAnswerService answerService;

  @Operation(summary = "根据回答的 id 获取对应的回答信息")
  @GetMapping("/id/{id}")
  public AnswerVo getAnswerById(
      @Parameter(description = "问题 id") @PathVariable Integer id,
      @RequestAttribute(value = "user", required = false) UserVo user) {
    return answerService.getAnswerByQuestionId(id, user);
  }

  @Operation(summary = "回答问题")
  @PostMapping("/")
  public Answer createAnswer(
      @Parameter(description = "回答的内容") @RequestBody CreateAnswerParam param,
      @RequestAttribute(value = "user") UserVo user) {
    return answerService.createAnswer(param, user);
  }

  @Operation(summary = "赞同回答")
  @PostMapping("/id/{answerId}/agree/")
  public void agree(
      @Parameter(description = "赞同的回答 id") @PathVariable("answerId") Integer answerId,
      @RequestAttribute(value = "user") UserVo user) {
    answerService.agree(answerId, user);
  }

  @Operation(summary = "取消赞同")
  @DeleteMapping("/id/{answerId}/agree/")
  public void removeAgree(
      @Parameter(description = "需要取消赞同的回答 id") @PathVariable("answerId") Integer answerId,
      @RequestAttribute(value = "user") UserVo user) {
    answerService.deleteAgree(answerId, user);
  }

  @Operation(summary = "获取推荐的回答内容")
  @GetMapping("/recommend")
  public List<AnswerVo> getRecommendAnswer(
      @Parameter(description = "需要推荐的数量") @RequestParam(defaultValue = "10") int num,
      @RequestAttribute(value = "user", required = false) UserVo user) {
    return answerService.getRecommendAnswer(num, user);
  }
}
