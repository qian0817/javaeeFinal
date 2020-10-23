package com.qianlei.zhifou.controller;

import com.qianlei.zhifou.entity.User;
import com.qianlei.zhifou.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/** @author qianlei */
@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired private IUserService userService;

  @GetMapping("/{id}")
  public Mono<User> getUser(@PathVariable Integer id) {
    return userService.getUserById(id);
  }

  @PostMapping("/")
  public Mono<User> register(@RequestBody User user) {
    user.setId(null);
    return userService.register(user);
  }
}
