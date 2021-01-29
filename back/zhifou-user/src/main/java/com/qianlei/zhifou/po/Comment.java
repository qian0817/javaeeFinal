package com.qianlei.zhifou.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** @author qianlei */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
  private Integer id;
  private Integer answerId;
  private String content;
  private Integer userId;
  private LocalDateTime createTime;
}
