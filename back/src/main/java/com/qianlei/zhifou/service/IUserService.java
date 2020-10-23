package com.qianlei.zhifou.service;

import com.qianlei.zhifou.entity.User;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import reactor.core.publisher.Mono;

public interface IUserService extends ReactiveUserDetailsService {
    Mono<User> getUserById(Integer id);

    Mono<User> register(User user);

}
