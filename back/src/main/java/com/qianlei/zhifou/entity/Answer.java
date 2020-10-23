package com.qianlei.zhifou.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/** @author qianlei */
@Table("zhifou_answer")
@Data
public class Answer {
  @Id private Integer id;
  private Integer userId;
  private String content;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;
}
