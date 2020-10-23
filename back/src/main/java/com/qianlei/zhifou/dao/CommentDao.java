package com.qianlei.zhifou.dao;

import com.qianlei.zhifou.entity.Comment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/** @author qianlei */
public interface CommentDao extends ReactiveCrudRepository<Comment, Integer> {}
