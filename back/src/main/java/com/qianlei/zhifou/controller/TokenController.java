package com.qianlei.zhifou.controller;

import cn.authing.core.types.User;
import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/** @author qianlei */
@RestController
@RequestMapping("/api/token")
public class TokenController {
  @Autowired private IUserService userService;

  @PostMapping("/")
  public User login(@RequestBody Map<String, Object> user, HttpServletResponse response) {
    String username = (String) user.get("username");
    String password = (String) user.get("password");
    var userInfo = userService.login(username, password);
    var token = userInfo.getToken();
    if (token != null) {
      // 设置 Cookie
      Cookie cookie = new Cookie("token", token);
      cookie.setMaxAge(3600 * 24 * 7);
      cookie.setHttpOnly(true);
      cookie.setSecure(true);
      response.addCookie(cookie);
    }
    return userInfo;
  }

  @GetMapping("/")
  public User getUserInfo(@CookieValue(required = false) String token) {
    if (token == null) {
      throw new ZhiFouException("用户未登录");
    }
    return userService.getUserInfo(token);
  }
}
