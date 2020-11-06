package com.qianlei.zhifou.controller;

import com.qianlei.zhifou.pojo.User;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/** @author qianlei */
@RestController
@RequestMapping("/api/user")
public class UserController {
  @Autowired private IUserService userService;

  @PostMapping("/")
  public UserVo register(@RequestBody User user, HttpSession session) {
    user = userService.register(user);
    session.setAttribute("user", user);
    return new UserVo(user);
  }
}
