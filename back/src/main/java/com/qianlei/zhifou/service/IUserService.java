package com.qianlei.zhifou.service;

import cn.authing.core.types.User;
import com.qianlei.zhifou.common.BaseResponse;
import reactor.core.publisher.Mono;

/**
 * @author qianlei
 */
public interface IUserService {
  /**
   * 注册
   *
   * @param username 用户名
   * @param password 密码
   * @return 用户信息
   */
  Mono<BaseResponse<User>> registerByUsername(String username, String password);


}
