package com.qianlei.zhifou.controller;

import cn.authing.core.types.User;
import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** @author qianlei */
@RestController
@RequestMapping("/api/token")
public class TokenController {
  @Autowired private IUserService userService;

  @GetMapping("/")
  public User getUserInfo(@CookieValue(required = false) String token) {
    if (token == null) {
      throw new ZhiFouException("用户未登录");
    }
    return userService.getUserInfo(token);
  }
}
