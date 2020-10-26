package com.qianlei.zhifou.config;

import cn.authing.core.graphql.GraphQLException;
import cn.authing.core.mgmt.ManagementClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/** @author qianlei */
@Configuration
public class AuthConfig {
  @Autowired private AuthingProperties properties;

  @Bean
  public ManagementClient managementClient() throws IOException, GraphQLException {
    var managementClient = new ManagementClient(properties.getId(), properties.getSecret());
    managementClient.requestToken().execute();
    return managementClient;
  }
}
