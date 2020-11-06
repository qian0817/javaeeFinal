package com.qianlei.zhifou.service;

import com.qianlei.zhifou.pojo.User;
import com.qianlei.zhifou.vo.UserVo;

/** @author qianlei */
public interface IUserService {
  /**
   * 根据用户 id 获取用户信息
   *
   * @param userId 用户id
   * @return 用户信息
   */
  UserVo getUserInfoByUserId(Integer userId);

  User register(User user);

  User login(User user);
}
