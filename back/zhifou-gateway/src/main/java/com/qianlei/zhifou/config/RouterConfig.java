package com.qianlei.zhifou.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** @author qianlei */
@Configuration
public class RouterConfig {

  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder
        .routes()
        .route(
            r ->
                r.path("/api/token/**", "/api/user/**")
                    .filters(GatewayFilterSpec::preserveHostHeader)
                    .uri("lb://zhifou-user"))
        .route(
            r ->
                r.path("/api/dynamic/**", "/api/followers/**")
                    .filters(GatewayFilterSpec::preserveHostHeader)
                    .uri("lb://zhifou-follow"))
        .route(
            r ->
                r.path("/api/answer/**", "/api/comment/**", "/api/question/**")
                    .filters(GatewayFilterSpec::preserveHostHeader)
                    .uri("lb://zhifou-question"))
        .build();
  }
}
