package com.qianlei.zhifou.controller;

import com.qianlei.zhifou.requestparam.CreateAnswerParam;
import com.qianlei.zhifou.service.IAnswerService;
import com.qianlei.zhifou.vo.AnswerVo;
import com.qianlei.zhifou.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/** @author qianlei */
@RestController
@RequestMapping("/api/answer")
public class AnswerController {
  @Resource private IAnswerService answerService;

  @Operation(summary = "根据回答的 id 获取对应的回答信息")
  @GetMapping("/id/{id}")
  public AnswerVo getAnswerById(
      @Parameter(description = "问题 id") @PathVariable Integer id,
      @AuthenticationPrincipal UserVo user) {
    return answerService.getAnswerByQuestionId(id, user);
  }

  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "回答问题")
  @PostMapping("/")
  public AnswerVo createAnswer(
      @Parameter(description = "回答的内容") @RequestBody CreateAnswerParam param,
      @AuthenticationPrincipal UserVo user) {
    return answerService.createAnswer(param, user);
  }

  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "赞同回答")
  @PostMapping("/id/{answerId}/agree/")
  public void agree(
      @Parameter(description = "赞同的回答 id") @PathVariable("answerId") Integer answerId,
      @AuthenticationPrincipal UserVo user) {
    answerService.agree(answerId, user);
  }

  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "取消赞同")
  @DeleteMapping("/id/{answerId}/agree/")
  public void removeAgree(
      @Parameter(description = "需要取消赞同的回答 id") @PathVariable("answerId") Integer answerId,
      @AuthenticationPrincipal UserVo user) {
    answerService.deleteAgree(answerId, user);
  }

  @Operation(summary = "获取推荐的回答内容")
  @GetMapping("/recommend")
  public List<AnswerVo> getRecommendAnswer(
      @Parameter(description = "需要推荐的数量") @RequestParam(defaultValue = "10") int num,
      @AuthenticationPrincipal UserVo user) {
    return answerService.getRecommendAnswer(num, user);
  }

  @Operation(summary = "搜索回答")
  @GetMapping("/keyword/{keyword}")
  public Page<AnswerVo> searchAnswer(
      @Parameter(description = "关键词") @PathVariable String keyword,
      @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
      @AuthenticationPrincipal UserVo user) {
    return answerService.searchAnswer(keyword, pageNum, pageSize, user);
  }

  @GetMapping("/user/{userId}/count")
  public Long countAnswerByUser(@PathVariable Integer userId) {
    return answerService.countAnswerByUser(userId);
  }


  @GetMapping("/agrees/user/{userId}/count")
  public Long countAgreeByUser(@PathVariable Integer userId){
    return answerService.countAgreeByUserId(userId);
  }
}
