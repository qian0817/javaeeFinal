package com.qianlei.zhifou.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** @author qianlei */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Answer {
  private Integer id;
  private Integer userId;
  private Integer questionId;
  private String content;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;
}
