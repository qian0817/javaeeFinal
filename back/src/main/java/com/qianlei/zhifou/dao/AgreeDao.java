package com.qianlei.zhifou.dao;

import com.qianlei.zhifou.pojo.Agree;
import org.springframework.data.jpa.repository.JpaRepository;

/** @author qianlei */
public interface AgreeDao extends JpaRepository<Agree, Integer> {
  long countByAnswerId(Integer answerId);

  long countByUserId(Integer userId);

  boolean existsByAnswerIdAndUserId(Integer answerId, Integer userId);

  void deleteByAnswerIdAndUserId(Integer answerId, Integer userId);
}
