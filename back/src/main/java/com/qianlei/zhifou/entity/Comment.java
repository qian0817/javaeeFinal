package com.qianlei.zhifou.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/** @author qianlei */
@Table(name = "zhifou_comment")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
  @Id private Integer id;

  @Column(name = "answer_id")
  private Integer answerId;

  @Column(name = "content")
  private String content;

  @Column(name = "user_id")
  private String userId;
}
