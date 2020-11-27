package com.qianlei.zhifou.dao;

import com.qianlei.zhifou.pojo.Agree;
import org.springframework.data.jpa.repository.JpaRepository;

/** @author qianlei */
public interface AgreeDao extends JpaRepository<Agree, Integer> {
  long countByAnswerId(String answerId);

  long countByUserId(Integer userId);

  boolean existsByAnswerIdAndUserId(String answerId, Integer userId);

  void deleteByAnswerIdAndUserId(String answerId, Integer userId);
}
