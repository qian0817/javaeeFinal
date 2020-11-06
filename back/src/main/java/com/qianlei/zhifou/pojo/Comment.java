package com.qianlei.zhifou.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/** @author qianlei */
@Table(name = "zhifou_comment")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "answer_id")
  private Integer answerId;

  @Column(name = "content")
  private String content;

  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "create_time")
  @CreationTimestamp
  private LocalDateTime createTime;
}
