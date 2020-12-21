package com.qianlei.zhifou.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/** @author qianlei */
@Table(
    name = "zhifou_answer",
    indexes = {
      @Index(name = "idx_user_id", columnList = "user_id"),
      @Index(name = "idx_question_id", columnList = "question_id"),
      @Index(name = "idx_question_id_and_user_id", columnList = "question_id, user_id")
    })
@Entity(name = "answer")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Answer {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "user_id", nullable = false)
  private Integer userId;

  @Column(name = "question_id", nullable = false)
  private Integer questionId;

  @Column(name = "content", columnDefinition = "TEXT", nullable = false)
  @Lob
  @Basic(fetch = FetchType.LAZY)
  private String content;

  @Column(name = "create_time", nullable = false)
  @CreationTimestamp
  private LocalDateTime createTime;

  @Column(name = "update_time", nullable = false)
  @UpdateTimestamp
  private LocalDateTime updateTime;
}
