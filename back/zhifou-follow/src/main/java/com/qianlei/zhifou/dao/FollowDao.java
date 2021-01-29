package com.qianlei.zhifou.dao;

import com.qianlei.zhifou.po.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowDao extends JpaRepository<Follow, Long> {
  boolean existsByFollowerUserIdAndFollowingUserId(Integer followerUserId, Integer followingUserId);

  List<Follow> findAllByFollowerUserId(Integer followerUserId);

  long countByFollowerUserId(int followerUserId);

  long countByFollowingUserId(Integer followingUserId);

  void deleteAllByFollowerUserIdAndFollowingUserId(Integer followerUserId, Integer followingUserId);
}
