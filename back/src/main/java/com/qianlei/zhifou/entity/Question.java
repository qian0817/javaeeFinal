package com.qianlei.zhifou.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/** @author qianlei */
@Table(name = "zhifou_question")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
  @Id private Integer id;

  @Column(name = "title")
  private String title;

  @Column(name = "tags")
  private String tags;

  @Column(name = "content")
  private String content;
}
