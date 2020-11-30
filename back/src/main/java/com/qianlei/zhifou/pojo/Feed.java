package com.qianlei.zhifou.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/** @author qianlei */
@Entity
@Table(name = "zhifou_feed")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Feed {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "create_user_id")
  private Integer createUserId;

  @Column(name = "event_id")
  private Long eventId;

  @Column(name = "create_time")
  private LocalDateTime createTime;
}
