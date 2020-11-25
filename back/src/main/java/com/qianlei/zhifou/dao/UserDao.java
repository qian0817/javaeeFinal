package com.qianlei.zhifou.dao;

import com.qianlei.zhifou.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/** @author qianlei */
public interface UserDao extends JpaRepository<User, Integer> {
  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);
}
