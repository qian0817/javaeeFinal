package com.qianlei.zhifou.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/** @author qianlei */
@Table(
    name = "zhifou_feed",
    indexes = {
      @Index(name = "idx_user_id_and_create_user_id", columnList = "user_id,create_user_id"),
      @Index(name = "idx_event_id", columnList = "event_id")
    })
@Entity(name = "feed")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feed {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Integer userId;

  @Column(name = "create_user_id", nullable = false)
  private Integer createUserId;

  @Column(name = "event_id", nullable = false)
  private Long eventId;

  @Column(name = "create_time", nullable = false)
  private LocalDateTime createTime;
}
