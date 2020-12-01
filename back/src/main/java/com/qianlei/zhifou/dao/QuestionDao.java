package com.qianlei.zhifou.dao;

import com.qianlei.zhifou.pojo.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionDao extends JpaRepository<Question, Integer> {

  @Query(value = "SELECT * FROM zhifou_question LIMIT ?,1", nativeQuery = true)
  Question findOne(long limit);

  Page<Question> findAllByContentContainsOrTitleContains(
      String content, String title, Pageable pageable);
}
