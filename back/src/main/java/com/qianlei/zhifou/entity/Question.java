package com.qianlei.zhifou.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/** @author qianlei */
@Table("zhifou_question")
@Data
public class Question {
  @Id private Integer id;
  private String title;
  private String tags;
  private String content;
}
