package com.qianlei.zhifou.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/** @author qianlei */
@Table(name = "zhifou_answer")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "user_id")
  private String userId;

  @Column(name = "question_id")
  private Integer questionId;

  @Column(name = "content")
  private String content;

  @Column(name = "create_time")
  @CreationTimestamp
  private LocalDateTime createTime;

  @Column(name = "update_time")
  @UpdateTimestamp
  private LocalDateTime updateTime;
}
