package com.qianlei.zhifou.filter;

import com.qianlei.zhifou.common.BaseResponse;
import com.qianlei.zhifou.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author qianlei
 */
@Order
@Slf4j
@Component
public class TokenFilter implements WebFilter {
    @Autowired
    private IUserService userService;

    @NotNull
    @Override
    public Mono<Void> filter(@NotNull ServerWebExchange exchange, @NotNull WebFilterChain chain) {
        var tokenCookie = exchange.getRequest().getCookies().getFirst("token");
        if (tokenCookie != null) {
            String token = tokenCookie.getValue();
            log.info("token " + token);
            // TODO 判断过期
            return userService
                    .refreshToken(token)
                    .doOnSuccess(
                            response -> {
                                var newToken = response.getData().getToken();
                                if (newToken == null) {
                                    return;
                                }
                                exchange.getResponse().getCookies().remove("token");
                                exchange
                                        .getResponse()
                                        .addCookie(
                                                ResponseCookie.from("token", newToken)
                                                        .httpOnly(true)
                                                        .secure(true)
                                                        .maxAge(3600 * 24 * 7)
                                                        .build());
                            })
                    .onErrorReturn(new BaseResponse<>())
                    .then(chain.filter(exchange));
        }

        return chain.filter(exchange);
    }
}
