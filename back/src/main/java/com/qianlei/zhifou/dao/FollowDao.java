package com.qianlei.zhifou.dao;

import com.qianlei.zhifou.pojo.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowDao extends JpaRepository<Follow, Long> {
  boolean existsByFollowerUserIdAndFollowingUserId(
      Integer followerUserId, Integer followingUserId);

  long countByFollowerUserId(int followerUserId);

  long countByFollowingUserId(Integer followingUserId);

  void deleteAllByFollowerUserIdAndFollowingUserId(Integer followerUserId, Integer followingUserId);
}
