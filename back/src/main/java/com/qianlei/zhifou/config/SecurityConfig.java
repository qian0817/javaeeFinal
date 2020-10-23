package com.qianlei.zhifou.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/** @author qianlei */
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {
  @Bean
  public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
    return http.csrf()
        .disable()
        .logout()
        .logoutUrl("/logout")
        .and()
        .formLogin()
        .and()
        .authorizeExchange()
        .anyExchange()
        .permitAll()
        .and()
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
