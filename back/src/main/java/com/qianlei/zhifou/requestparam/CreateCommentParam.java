package com.qianlei.zhifou.requestparam;

import com.qianlei.zhifou.pojo.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** @author qianlei */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCommentParam {
  private String content;

  public Comment toComment(Integer answerId) {
    var comment = new Comment();
    comment.setAnswerId(answerId);
    comment.setContent(content);
    return comment;
  }
}
