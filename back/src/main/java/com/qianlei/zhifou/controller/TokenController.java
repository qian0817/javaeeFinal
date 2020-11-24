package com.qianlei.zhifou.controller;

import com.nimbusds.jose.jwk.RSAKey;
import com.qianlei.zhifou.pojo.User;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.utils.JwtUtils;
import com.qianlei.zhifou.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** @author qianlei */
@RestController
@RequestMapping("/api/token")
public class TokenController {
  @Autowired private IUserService userService;
  @Autowired private RSAKey rsaKey;

  @PostMapping("/")
  public String login(@RequestBody User user) {
    user = userService.login(user);
    return JwtUtils.createJwt(new UserVo(user), rsaKey);
  }

  @GetMapping("/")
  public UserVo getUserInfo(@RequestAttribute("user") UserVo user) {
    return user;
  }
}
