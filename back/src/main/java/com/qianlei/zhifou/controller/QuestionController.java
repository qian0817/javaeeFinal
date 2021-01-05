package com.qianlei.zhifou.controller;

import com.qianlei.zhifou.pojo.Question;
import com.qianlei.zhifou.requestparam.CreateQuestionParam;
import com.qianlei.zhifou.service.IAnswerService;
import com.qianlei.zhifou.service.IQuestionService;
import com.qianlei.zhifou.vo.AnswerVo;
import com.qianlei.zhifou.vo.QuestionHotVo;
import com.qianlei.zhifou.vo.QuestionVo;
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
@RequestMapping("/api/question")
public class QuestionController {
  @Resource private IQuestionService questionService;
  @Resource private IAnswerService answerService;

  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "创建新问题")
  @PostMapping("/")
  public Question createQuestion(
      @Parameter(description = "新问题的内容") @RequestBody CreateQuestionParam param) {
    return questionService.createQuestion(param);
  }

  @Operation(summary = "根据问题 id 获取问题")
  @GetMapping("/id/{id}")
  public QuestionVo getQuestionById(
      @Parameter(description = "问题id") @PathVariable Integer id,
      @AuthenticationPrincipal UserVo user) {
    return questionService.getQuestionVoById(id, user);
  }

  @Operation(summary = "根据问题 id 获取回答信息")
  @GetMapping("/id/{id}/answers/")
  public Page<AnswerVo> getAnswerByQuestionId(
      @Parameter(description = "问题id") @PathVariable("id") Integer questionId,
      @Parameter(description = "顺序倒序")
          @RequestParam(value = "sort_direction", defaultValue = "desc")
          String sortDirection,
      @Parameter(description = "排序依据") @RequestParam(value = "sort_by", defaultValue = "createTime")
          String sortBy,
      @Parameter(description = "页码") @RequestParam(value = "pageNum", defaultValue = "0")
          int pageNum,
      @Parameter(description = "每页数量") @RequestParam(value = "pageSize", defaultValue = "10")
          int pageSize,
      @AuthenticationPrincipal UserVo user) {
    return answerService.getAllAnswerByQuestionId(
        questionId, sortDirection, sortBy, pageNum, pageSize, user);
  }

  @Operation(summary = "随机获取问题")
  @GetMapping("/random")
  public List<Question> getRandomQuestion(
      @Parameter(description = "需要获取的问题数量") @RequestParam(defaultValue = "10") int num) {
    return questionService.getRandomQuestion(num);
  }

  @Operation(summary = "获取最火热的问题（热榜）")
  @GetMapping("/hot/")
  public List<QuestionHotVo> getHottestQuestion() {
    return questionService.getHottestQuestion(30);
  }

  @Operation(summary = "搜索问题")
  @GetMapping("/keyword/{keyword}")
  public Page<Question> searchQuestion(
      @Parameter(description = "关键词") @PathVariable String keyword,
      @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
    return questionService.searchQuestion(keyword, pageNum, pageSize);
  }
}
