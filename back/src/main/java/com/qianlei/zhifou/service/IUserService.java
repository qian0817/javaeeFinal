package com.qianlei.zhifou.service;

import cn.authing.core.types.JwtTokenStatus;
import cn.authing.core.types.RefreshToken;
import cn.authing.core.types.User;
import com.qianlei.zhifou.vo.UserVo;

/** @author qianlei */
public interface IUserService {

  /**
   * 根据token获取用户信息
   *
   * @param token token
   * @return 用户信息
   */
  User getUserInfo(String token);

  /**
   * 刷新token
   *
   * @param oldToken 旧的token
   * @return 新的token
   */
  RefreshToken refreshToken(String oldToken);

  /**
   * 根据用户 id 获取用户信息
   *
   * @param userId 用户id
   * @return 用户信息
   */
  UserVo getUserInfoByUserId(String userId);

  /**
   * 获取 jwt 的状态信息
   * @param token jwt 的 token
   * @return 状态信息
   */
  JwtTokenStatus getJwtStatus(String token);
}
