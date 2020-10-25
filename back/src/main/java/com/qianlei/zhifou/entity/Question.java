package com.qianlei.zhifou.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/** @author qianlei */
@Table(name = "zhifou_question")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "title")
  private String title;

  @Column(name = "tags")
  private String tags;

  @Column(name = "content")
  private String content;
}
