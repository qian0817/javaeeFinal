package com.qianlei.zhifou.service.impl;

import cn.authing.core.auth.AuthenticationClient;
import cn.authing.core.graphql.GraphQLException;
import cn.authing.core.mgmt.ManagementClient;
import cn.authing.core.types.JwtTokenStatus;
import cn.authing.core.types.RefreshToken;
import cn.authing.core.types.User;
import com.qianlei.zhifou.common.AuthorizationException;
import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.config.AuthingProperties;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
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
    var client = new AuthenticationClient(authingProperties.getId());
    try {
      client.setAccessToken(token);
      return client.getCurrentUser().execute();
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
    try {
      client.setAccessToken(oldToken);
      return client.refreshToken().execute();
    } catch (IOException e) {
      log.error("请求 authing 服务器错误", e);
      throw new RuntimeException(e);
    } catch (GraphQLException e) {
      throw mapAuthorizationException(e);
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

  @Override
  public JwtTokenStatus getJwtStatus(String token) {
    var client = new AuthenticationClient(authingProperties.getId());
    try {
      client.setAccessToken(token);
      return client.checkLoginStatus().execute();
    } catch (IOException e) {
      log.error("请求 authing 服务器错误", e);
      throw new RuntimeException(e);
    } catch (GraphQLException e) {
      throw mapGraphException(e);
    }
  }

  private ZhiFouException mapGraphException(GraphQLException e) {
    return new ZhiFouException(getMessageFromGraphException(e));
  }

  /**
   * 截取 GraphQLException 中的 message 字段
   *
   * @param e GraphQLException
   * @return GraphQLException 中的message信息
   */
  @NotNull
  private String getMessageFromGraphException(GraphQLException e) {
    return e.getMessage().substring(52, e.getMessage().length() - 4);
  }

  private AuthorizationException mapAuthorizationException(GraphQLException e) {
    return new AuthorizationException(getMessageFromGraphException(e));
  }
}
