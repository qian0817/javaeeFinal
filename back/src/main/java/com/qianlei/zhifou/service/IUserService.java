package com.qianlei.zhifou.service;

import cn.authing.core.types.RefreshToken;
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

  /**
   * 用户登录
   *
   * @param username 用户名
   * @param password 密码
   * @return 登录信息
   */
  Mono<BaseResponse<User>> login(String username, String password);

  /**
   * 根据token获取用户信息
   *
   * @param token token
   * @return 用户信息
   */
  Mono<BaseResponse<User>> getUserInfo(String token);

  /**
   * 刷新token
   *
   * @param oldToken 旧的token
   * @return 新的token
   */
  Mono<BaseResponse<RefreshToken>> refreshToken(String oldToken);
}
