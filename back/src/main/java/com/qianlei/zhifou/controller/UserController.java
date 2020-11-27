package com.qianlei.zhifou.controller;

import com.nimbusds.jose.jwk.RSAKey;
import com.qianlei.zhifou.requestparam.RegisterParam;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.utils.JwtUtils;
import com.qianlei.zhifou.vo.UserInfo;
import com.qianlei.zhifou.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/** @author qianlei */
@RestController
@RequestMapping("/api/user")
public class UserController {
  @Autowired private IUserService userService;
  @Autowired private RSAKey rsaKey;

  @PostMapping("/")
  public String register(@RequestBody RegisterParam param) {
    var user = userService.register(param);
    return JwtUtils.createJwt(new UserVo(user), rsaKey);
  }

  @PostMapping("/registerCode/")
  public void sendEmail(@RequestBody Map<String, String> map) {
    var email = map.get("email");
    userService.sendRegisterEmail(email);
  }

  @GetMapping("/{id}")
  public UserInfo getUserInfo(
      @PathVariable("id") Integer id,
      @RequestAttribute(value = "user", required = false) UserVo user) {
    return userService.getUserInfoByUserId(id, user);
  }

  @PostMapping("/{follower}/following/")
  public void follow(
      @PathVariable("follower") Integer follower, @RequestAttribute("user") UserVo following) {
    userService.follow(follower, following.getId());
  }

  @DeleteMapping("/{follower}/following/")
  public void unfollow(
      @PathVariable("follower") Integer follower, @RequestAttribute("user") UserVo following) {
    userService.unfollow(follower, following.getId());
  }
}
