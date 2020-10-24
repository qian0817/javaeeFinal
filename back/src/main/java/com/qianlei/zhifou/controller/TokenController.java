package com.qianlei.zhifou.controller;

import cn.authing.core.types.User;
import com.qianlei.zhifou.common.BaseResponse;
import com.qianlei.zhifou.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

/**
 * @author qianlei
 */
@RestController
@RequestMapping("/api/token")
public class TokenController {
  @Autowired
  private IUserService userService;

  @PostMapping("/")
  public Mono<BaseResponse<User>> login(
          @RequestBody Map<String, Object> user, ServerWebExchange exchange) {
    String username = (String) user.get("username");
    String password = (String) user.get("password");
    var userInfo = userService.login(username, password);
    return userInfo.doOnSuccess(
            u ->
                    exchange
                            .getResponse()
                            .addCookie(
                                    ResponseCookie.from("token", Objects.requireNonNull(u.getData().getToken()))
                                            .build()));
  }

  @GetMapping("/")
  public Mono<BaseResponse<User>> getUserInfo(@CookieValue("token") String token) {
    return userService.getUserInfo(token);
  }
}
