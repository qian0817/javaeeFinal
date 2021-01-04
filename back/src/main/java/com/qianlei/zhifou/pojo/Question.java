package com.qianlei.zhifou.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/** @author qianlei */
@Table(name = "zhifou_question")
@Entity(name = "question")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Question {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "tags", nullable = false)
  private String tags;

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
