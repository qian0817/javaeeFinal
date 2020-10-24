package com.qianlei.zhifou.service.impl;

import cn.authing.core.auth.AuthenticationClient;
import cn.authing.core.types.RegisterByUsernameInput;
import cn.authing.core.types.User;
import com.qianlei.zhifou.common.BaseResponse;
import com.qianlei.zhifou.config.AuthingProperties;
import com.qianlei.zhifou.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
    var response = client.registerByUsername(new RegisterByUsernameInput(username, password));
    return Mono.fromCallable(response::execute).map(BaseResponse::new);
  }
}
