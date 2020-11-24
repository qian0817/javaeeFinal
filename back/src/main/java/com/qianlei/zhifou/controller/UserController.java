package com.qianlei.zhifou.controller;

import com.nimbusds.jose.jwk.RSAKey;
import com.qianlei.zhifou.pojo.User;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.utils.JwtUtils;
import com.qianlei.zhifou.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** @author qianlei */
@RestController
@RequestMapping("/api/user")
public class UserController {
  @Autowired private IUserService userService;
  @Autowired private RSAKey rsaKey;

  @PostMapping("/")
  public String register(@RequestBody User user) {
    user = userService.register(user);
    return JwtUtils.createJwt(new UserVo(user), rsaKey);
  }
}
