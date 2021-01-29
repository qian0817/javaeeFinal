package com.qianlei.zhifou.controller;

import com.nimbusds.jose.jwk.RSAKey;
import com.qianlei.zhifou.requestparam.UserLoginParam;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.utils.JwtUtils;
import com.qianlei.zhifou.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/** @author qianlei */
@RestController
@RequestMapping("/api/token")
public class TokenController {
  @Resource private IUserService userService;
  @Resource private RSAKey rsaKey;

  @Operation(
      summary = "用户登录",
      responses = {@ApiResponse(description = "jwt 字符串")})
  @PostMapping("/")
  public String login(@Parameter(description = "需要登陆的用户信息") @RequestBody UserLoginParam param) {
    var user = userService.login(param);
    return JwtUtils.createJwt(new UserVo(user), rsaKey);
  }

  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "获取当前登录用户信息")
  @GetMapping("/")
  public UserVo getUserInfo(@AuthenticationPrincipal UserVo user) {
    return user;
  }
}
