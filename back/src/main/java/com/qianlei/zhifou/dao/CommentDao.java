package com.qianlei.zhifou.dao;

import com.qianlei.zhifou.pojo.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/** @author qianlei */
public interface CommentDao extends JpaRepository<Comment, Integer> {
  Page<Comment> findAllByAnswerIdOrderByCreateTimeDesc(Integer answerId, Pageable pageable);
}
