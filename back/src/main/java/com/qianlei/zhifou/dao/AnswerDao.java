package com.qianlei.zhifou.dao;

import com.qianlei.zhifou.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/** @author qianlei */
public interface AnswerDao extends JpaRepository<Answer, Integer> {
  Page<Answer> findAllByQuestionId(Integer questionId, Pageable pageable);
}
