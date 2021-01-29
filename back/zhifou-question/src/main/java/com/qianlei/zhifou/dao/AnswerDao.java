package com.qianlei.zhifou.dao;

import com.qianlei.zhifou.po.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/** @author qianlei */
public interface AnswerDao extends JpaRepository<Answer, Integer> {
  Page<Answer> findAllByQuestionId(Integer questionId, Pageable page);

  long countByUserId(Integer userId);

  Answer findByUserIdAndQuestionId(Integer userId, Integer questionId);

  @Query(value = "SELECT * FROM zhifou_answer LIMIT ?,1", nativeQuery = true)
  Answer findOne(long offset);
}
