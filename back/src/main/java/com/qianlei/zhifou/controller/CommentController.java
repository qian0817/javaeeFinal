package com.qianlei.zhifou.controller;

import com.qianlei.zhifou.requestparam.CreateCommentParam;
import com.qianlei.zhifou.service.ICommentService;
import com.qianlei.zhifou.vo.CommentVo;
import com.qianlei.zhifou.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/** @author qianlei */
@RestController
@RequestMapping("/api/comment")
public class CommentController {
  @Autowired private ICommentService commentService;

  @Operation(summary = "根据回答 id 获取评论信息，结果以分页信息展示")
  @GetMapping("/answer/{answerId}")
  public Page<CommentVo> getCommentVo(
      @Parameter(description = "回答 id") @PathVariable Integer answerId,
      @Parameter(description = "需要查看的页数，从0开始") @RequestParam(value = "pageNum", defaultValue = "0")
          Integer pageNum,
      @Parameter(description = "每页的数量") @RequestParam(value = "pageSize", defaultValue = "10")
          Integer pageSize) {
    return commentService.getComment(answerId, pageNum, pageSize);
  }

  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "创建新的评论")
  @PostMapping("/answer/{answerId}")
  public CommentVo createComment(
      @Parameter(description = "回答id") @PathVariable("answerId") Integer answerId,
      @Parameter(description = "评论信息") @RequestBody CreateCommentParam param,
      @AuthenticationPrincipal UserVo user) {
    return commentService.createNewComment(param, answerId, user);
  }
}
