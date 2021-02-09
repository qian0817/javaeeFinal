package com.qianlei.zhifou.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/** @author qianlei */
@Table(
    name = "zhifou_comment",
    indexes = {
      @Index(name = "idx_answer_id_and_create_time", columnList = "answer_id, create_time")
    })
@Entity(name = "comment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "answer_id", nullable = false)
  private Integer answerId;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "user_id", nullable = false)
  private Integer userId;

  @Column(name = "create_time", nullable = false)
  @CreationTimestamp
  private LocalDateTime createTime;
}
