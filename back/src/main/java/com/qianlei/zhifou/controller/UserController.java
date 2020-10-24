package com.qianlei.zhifou.controller;

import cn.authing.core.types.User;
import com.qianlei.zhifou.common.BaseResponse;
import com.qianlei.zhifou.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author qianlei
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
  @Autowired
  private IUserService userService;

  @PostMapping("/")
  public Mono<BaseResponse<User>> register(@RequestBody Map<String, Object> user) {
    String username = (String) user.get("username");
    String password = (String) user.get("password");
    return userService.registerByUsername(username, password);
  }
}
