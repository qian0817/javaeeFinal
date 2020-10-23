package com.qianlei.zhifou.dao;

import com.qianlei.zhifou.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/** @author qianlei */
public interface UserDao extends ReactiveCrudRepository<User, Integer> {
  Mono<User> findByUsername(String username);

}
