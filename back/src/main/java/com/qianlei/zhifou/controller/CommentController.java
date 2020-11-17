package com.qianlei.zhifou.controller;

import com.qianlei.zhifou.common.AuthorizationException;
import com.qianlei.zhifou.pojo.Comment;
import com.qianlei.zhifou.pojo.User;
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
      @PathVariable String answerId,
      @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
    return commentService.getComment(answerId, pageNum, pageSize);
  }

  @PostMapping("/answer/{answerId}")
  public CommentVo createComment(
      @PathVariable("answerId") String answerId,
      @RequestBody Comment comment,
      @SessionAttribute(value = "user", required = false) User user) {
    if (user == null) {
      throw new AuthorizationException("用户未登录");
    }
    comment.setAnswerId(answerId);
    return commentService.createNewComment(comment, user);
  }
}
