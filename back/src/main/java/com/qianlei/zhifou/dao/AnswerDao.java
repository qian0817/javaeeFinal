package com.qianlei.zhifou.dao;

import com.qianlei.zhifou.entity.Agree;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/** @author qianlei */
public interface AnswerDao extends ReactiveCrudRepository<Agree, Integer> {}
