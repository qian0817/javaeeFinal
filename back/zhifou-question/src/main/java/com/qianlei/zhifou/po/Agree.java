package com.qianlei.zhifou.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/** @author qianlei */
@Table(
    name = "zhifou_agree",
    indexes = {
      @Index(name = "idx_answer_id", columnList = "answer_id"),
      @Index(name = "idx_user_id", columnList = "user_id"),
      @Index(name = "idx_user_id_and_answer_id", columnList = "user_id, answer_id")
    })
@Entity(name = "agree")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agree {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "user_id", nullable = false)
  private Integer userId;

  @Column(name = "answer_id", nullable = false)
  private Integer answerId;
}
