package com.qianlei.zhifou.controller;

import com.qianlei.zhifou.entity.Comment;
import com.qianlei.zhifou.service.ICommentService;
import com.qianlei.zhifou.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/** @author qianlei */
@RestController
@RequestMapping("/api/comment")
public class CommentController {
  @Autowired private ICommentService commentService;

  @GetMapping("/answer/{answerId}")
  public Page<CommentVo> getCommentVo(
      @PathVariable Integer answerId,
      @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
    return commentService.getComment(answerId, pageNum, pageSize);
  }

  @PostMapping("/answer/{answerId}")
  public CommentVo createComment(
      @PathVariable("answerId") Integer answerId,
      @RequestBody Comment comment,
      @CookieValue String token) {
    comment.setAnswerId(answerId);
    return commentService.createNewComment(comment, token);
  }
}
