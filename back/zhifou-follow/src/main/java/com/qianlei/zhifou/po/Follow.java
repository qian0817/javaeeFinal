package com.qianlei.zhifou.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/** @author qianlei */
@Table(
    name = "zhifou_follow",
    indexes = {
      @Index(name = "idx_follower_user_id", columnList = "follower_user_id"),
      @Index(name = "idx_following_user_id", columnList = "following_user_id"),
      @Index(
          name = "idx_following_user_id_and_follower_user_id",
          columnList = "following_user_id, follower_user_id")
    })
@Entity(name = "follow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Follow {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /** 被关注者 */
  @Column(name = "follower_user_id", nullable = false)
  private Integer followerUserId;
  /** 关注者 */
  @Column(name = "following_user_id", nullable = false)
  private Integer followingUserId;
}
