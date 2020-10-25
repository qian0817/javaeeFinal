package com.qianlei.zhifou.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** @author qianlei */
@Component
@ConfigurationProperties("authing.userpool")
@Data
public class AuthingProperties {
  private String id;
  private String secret;
}
