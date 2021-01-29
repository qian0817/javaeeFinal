package com.qianlei.zhifou.dao;

import com.qianlei.zhifou.po.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedDao extends JpaRepository<Feed, Long> {
  Page<Feed> findAllByUserId(Integer userId, Pageable pageable);

  void deleteAllByEventId(Long eventId);

  void deleteAllByCreateUserIdAndUserId(Integer createUserId,Integer userId);
}
