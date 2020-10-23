package com.qianlei.zhifou.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/** @author qianlei */
@Table("zhifou_comment")
@Data
public class Comment {
  @Id private Integer id;
  private Integer answerId;
  private String content;
  private Integer userId;
}
