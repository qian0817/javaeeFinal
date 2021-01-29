package com.qianlei.zhifou.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** @author qianlei */
@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI openApi() {
    return new OpenAPI().info(new Info().title("知否 API").description("知否文档").version("v1.0.0"));
  }
}
