package com.qianlei.zhifou.dao;

import com.qianlei.zhifou.po.UserEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** @author qianlei */
public interface UserEventDao extends JpaRepository<UserEvent, Long> {
  UserEvent findByUserIdAndOperationAndTableNameAndTableId(
      Integer userId, Integer operation, String tableName, Integer tableId);

  Page<UserEvent> findAllByUserId(Integer userId, Pageable pageable);

  List<UserEvent> findAllByUserId(Integer userId);
}
