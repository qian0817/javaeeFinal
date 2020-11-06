package com.qianlei.zhifou.vo;

import com.qianlei.zhifou.pojo.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** @author qianlei */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVo {
  private UserVo userVo;
  private Comment comment;
}
