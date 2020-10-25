package com.qianlei.zhifou.dao;

import com.qianlei.zhifou.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** @author qianlei */
public interface AnswerDao extends JpaRepository<Answer, Integer> {
  List<Answer> findAllByQuestionId(Integer questionId);
}
