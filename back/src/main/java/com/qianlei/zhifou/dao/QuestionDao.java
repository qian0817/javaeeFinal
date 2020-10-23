package com.qianlei.zhifou.dao;

import com.qianlei.zhifou.entity.Question;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/** @author qianlei */
public interface QuestionDao extends ReactiveCrudRepository<Question, Integer> {}
