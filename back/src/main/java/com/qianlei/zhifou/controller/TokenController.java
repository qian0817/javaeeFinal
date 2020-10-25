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
                u -> {
                    var token = u.getData().getToken();
                    if (token == null) {
                        return;
                    }
                    // 设置cookie
                    exchange
                            .getResponse()
                            .addCookie(
                                    ResponseCookie.from("token", token)
                                            .httpOnly(true)
                                            .secure(true)
                                            .maxAge(3600 * 24 * 7)
                                            .build());
                });
    }

    @GetMapping("/")
    public Mono<BaseResponse<User>> getUserInfo(ServerWebExchange exchange) {
        var token = exchange.getRequest().getCookies().getFirst("token");
        if (token == null) {
            return Mono.just(new BaseResponse<>(1, "用户未登录"));
        }
        return userService.getUserInfo(token.getValue());
    }
}
