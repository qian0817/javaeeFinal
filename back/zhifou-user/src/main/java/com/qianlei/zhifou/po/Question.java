package com.qianlei.zhifou.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** @author qianlei */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Question {
  private Integer id;
  private String title;
  private String tags;
  private String content;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;
}
