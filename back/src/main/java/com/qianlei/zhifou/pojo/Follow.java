package com.qianlei.zhifou.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/** @author qianlei */
@Entity
@Table(name = "zhifou_follow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Follow {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /** 被关注者 */
  @Column(name = "follower_user_id")
  private Integer followerUserId;
  /** 关注者 */
  @Column(name = "following_user_id")
  private Integer followingUserId;
}
