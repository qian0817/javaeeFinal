package com.qianlei.zhifou.service.impl;

import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.UserDao;
import com.qianlei.zhifou.entity.User;
import com.qianlei.zhifou.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/** @author qianlei */
@Service
public class UserServiceImpl implements IUserService {
  @Autowired private UserDao userDao;
  @Autowired private PasswordEncoder passwordEncoder;

  @Override
  public Mono<UserDetails> findByUsername(String username) {
    System.out.println(username);
    return userDao.findByUsername(username).cast(UserDetails.class);
  }

  @Override
  public Mono<User> getUserById(Integer id) {
    return userDao.findById(id);
  }

  @Override
  public Mono<User> register(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userDao.save(user);
  }
}
