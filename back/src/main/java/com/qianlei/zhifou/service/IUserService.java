package com.qianlei.zhifou.service;

import com.qianlei.zhifou.pojo.User;
import com.qianlei.zhifou.requestparam.RegisterParam;
import com.qianlei.zhifou.requestparam.UserLoginParam;
import com.qianlei.zhifou.vo.UserInfo;
import com.qianlei.zhifou.vo.UserVo;
import org.jetbrains.annotations.Nullable;

/** @author qianlei */
public interface IUserService {
  /**
   * 根据用户 id 获取用户信息
   *
   * @param userId 用户id
   * @return 用户信息
   */
  UserVo getUserInfoByUserId(Integer userId);

  /**
   * 获取指定用户 id 对应的用户
   *
   * @param userId 用户 id
   * @param user 想要获取该用户信息的用户 可为空
   * @return 用户信息
   */
  UserInfo getUserInfoByUserId(Integer userId, @Nullable UserVo user);

  /**
   * 用户注册
   *
   * @param user 用户注册的信息，包括用户名密码邮箱以及验证码
   * @return 注册的用户信息
   */
  User register(RegisterParam user);

  /**
   * 向指定的电子邮箱发送注册验证码信息
   *
   * @param email 注册者的邮箱
   */
  void sendRegisterEmail(String email);

  /**
   * 用户登录
   *
   * @param param 包括用户名和密码
   * @return 完整的用户登录信息
   */
  User login(UserLoginParam param);

  /**
   * 关注某人
   *
   * @param followerUserId 被关注者用户 id
   * @param followingUserId 关注者用户 id
   */
  void follow(Integer followerUserId, Integer followingUserId);
  /**
   * 取消关注某人
   *
   * @param unfollowerUserId 被取消关注者用户 id
   * @param unfollowingUserId 取消关注者用户 id
   */
  void unfollow(Integer unfollowerUserId, Integer unfollowingUserId);
}
