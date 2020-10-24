package com.qianlei.zhifou.service.impl;

import cn.authing.core.auth.AuthenticationClient;
import cn.authing.core.graphql.GraphQLException;
import cn.authing.core.types.LoginByUsernameInput;
import cn.authing.core.types.RegisterByUsernameInput;
import cn.authing.core.types.User;
import com.qianlei.zhifou.common.BaseResponse;
import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.config.AuthingProperties;
import com.qianlei.zhifou.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author qianlei
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {
  @Autowired
  private AuthingProperties authingProperties;

  @Override
  public Mono<BaseResponse<User>> registerByUsername(String username, String password) {
    var client = new AuthenticationClient(authingProperties.getId());
    var request = client.registerByUsername(new RegisterByUsernameInput(username, password));
    return Mono.fromCallable(request::execute)
            .map(BaseResponse::new)
            .onErrorMap(
                    GraphQLException.class,
                    e -> {
                      // 截取其中的message字段信息
                      var message = e.getMessage().substring(52, e.getMessage().length() - 4);
                      return new ZhiFouException(message);
                    });
  }

  @Override
  public Mono<BaseResponse<User>> login(String username, String password) {
    var client = new AuthenticationClient(authingProperties.getId());
    var request = client.loginByUsername(new LoginByUsernameInput(username, password));
    return Mono.fromCallable(request::execute).map(BaseResponse::new);
  }

  @Override
  public Mono<BaseResponse<User>> getUserInfo(String token) {
    log.info("getUserInfo");
    var client = new AuthenticationClient(authingProperties.getId());
    client.setAccessToken(token);
    var request = client.getCurrentUser();
    return Mono.fromCallable(request::execute).map(BaseResponse::new);
  }

  @Override
  public Mono<BaseResponse<String>> refreshToken(String oldToken) {
    var client = new AuthenticationClient(authingProperties.getId());
    client.setAccessToken(oldToken);
    log.info("refreshToken");
    var request = client.refreshToken();
    return Mono.fromCallable(request::execute)
            .map(token -> Objects.requireNonNull(token.getToken()))
            .map(BaseResponse::new);
  }
}
