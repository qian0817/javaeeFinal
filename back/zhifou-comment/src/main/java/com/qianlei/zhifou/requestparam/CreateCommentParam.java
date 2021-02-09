package com.qianlei.zhifou.requestparam;

import com.qianlei.zhifou.po.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** @author qianlei */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCommentParam {
  private String content;

  public Comment toComment(Integer answerId, Integer userId) {
    var comment = new Comment();
    comment.setAnswerId(answerId);
    comment.setContent(content);
    comment.setUserId(userId);
    return comment;
  }
}
