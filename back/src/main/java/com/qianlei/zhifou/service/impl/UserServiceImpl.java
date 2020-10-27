package com.qianlei.zhifou.service.impl;

import cn.authing.core.auth.AuthenticationClient;
import cn.authing.core.graphql.GraphQLException;
import cn.authing.core.mgmt.ManagementClient;
import cn.authing.core.types.RefreshToken;
import cn.authing.core.types.User;
import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.config.AuthingProperties;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/** @author qianlei */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {
  @Autowired private AuthingProperties authingProperties;
  @Autowired private ManagementClient managementClient;

  @Override
  public User getUserInfo(String token) {
    log.info("getUserInfo");
    var client = new AuthenticationClient(authingProperties.getId());
    client.setAccessToken(token);
    var request = client.getCurrentUser();
    try {
      return request.execute();
    } catch (IOException e) {
      log.error("请求 authing 服务器错误", e);
      throw new RuntimeException(e);
    } catch (GraphQLException e) {
      throw mapGraphException(e);
    }
  }

  @Override
  public RefreshToken refreshToken(String oldToken) {
    var client = new AuthenticationClient(authingProperties.getId());
    client.setAccessToken(oldToken);
    log.info("refreshToken");
    var request = client.refreshToken();
    try {
      return request.execute();
    } catch (IOException e) {
      log.error("请求 authing 服务器错误", e);
      throw new RuntimeException(e);
    } catch (GraphQLException e) {
      throw mapGraphException(e);
    }
  }

  @Override
  public UserVo getUserInfoByUserId(String userId) {
    try {
      var user = managementClient.users().detail(userId).execute();
      return new UserVo(user.getId(), user.getUsername());
    } catch (IOException e) {
      log.error("请求 authing 服务器错误", e);
      throw new RuntimeException(e);
    } catch (GraphQLException e) {
      throw mapGraphException(e);
    }
  }

  private ZhiFouException mapGraphException(GraphQLException e) {
    // 截取 GraphQLException 中的 message 字段
    var message = e.getMessage().substring(52, e.getMessage().length() - 4);
    return new ZhiFouException(message);
  }
}
