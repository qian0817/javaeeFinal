package com.qianlei.zhifou.dao;

import com.qianlei.zhifou.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/** @author qianlei */
public interface CommentDao extends JpaRepository<Comment, Integer> {}
