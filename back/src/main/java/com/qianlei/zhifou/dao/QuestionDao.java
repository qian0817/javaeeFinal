package com.qianlei.zhifou.dao;

import com.qianlei.zhifou.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/** @author qianlei */
public interface QuestionDao extends JpaRepository<Question, Integer> {

  @Query(
      value = "SELECT id, title, tags, content FROM zhifou_question ORDER BY rand() LIMIT :n",
      nativeQuery = true)
  List<Question> findRandomQuestion(@Param("n") int n);

  Page<Question> findAllByTitleContaining(String title, Pageable pageable);
}
