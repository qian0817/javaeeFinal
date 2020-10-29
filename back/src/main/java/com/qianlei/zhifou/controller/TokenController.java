package com.qianlei.zhifou.controller;

import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.vo.UserVo;
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
  public UserVo getUserInfo(@CookieValue(required = false) String token) {
    if (token == null) {
      throw new ZhiFouException("用户未登录");
    }
    return new UserVo(userService.getUserInfo(token));
  }
}
