package com.qianlei.zhifou.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/** @author qianlei */
@Table("zhifou_agree")
@Data
public class Agree {
  @Id private Integer id;

  @Column("user_id")
  private Integer userId;

  @Column("answer_id")
  private Integer answerId;
}
