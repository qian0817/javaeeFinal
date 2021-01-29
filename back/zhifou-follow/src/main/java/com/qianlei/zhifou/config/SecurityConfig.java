package com.qianlei.zhifou.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import com.qianlei.zhifou.vo.UserVo;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.qianlei.zhifou.common.Constant.SecurityConstant.TOKEN_HEADER;
import static com.qianlei.zhifou.common.Constant.SecurityConstant.TOKEN_PREFIX;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

/** @author qianlei */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .authorizeRequests()
        // 其他都放行
        .anyRequest()
        .permitAll()
        .and()
        // 添加自定义Filter
        .addFilter(new JwtAuthorizationFilter(authenticationManager()))
        // 不需要session（不创建会话）
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        // 授权异常处理
        .exceptionHandling()
        .authenticationEntryPoint(((req, resp, ex) -> resp.sendError(SC_UNAUTHORIZED, "用户未登录")))
        .accessDeniedHandler(((req, resp, ex) -> resp.sendError(SC_FORBIDDEN, "无权限访问")));
  }

  public static class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
      super(authenticationManager);
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
      var authorization = request.getHeader(TOKEN_HEADER);
      if (StringUtils.startsWithIgnoreCase(authorization, TOKEN_PREFIX)) {
        var jwtToken = authorization.substring(7);
        var signedJwt = SignedJWT.parse(jwtToken);
        ObjectMapper mapper = new ObjectMapper();
        var user =
                mapper.readValue(
                        mapper.writeValueAsString(signedJwt.getJWTClaimsSet().getJSONObjectClaim("user")),
                        UserVo.class);
        if (user != null) {
          var auth = new UsernamePasswordAuthenticationToken(user, jwtToken, List.of());
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      }
      chain.doFilter(request, response);
    }
  }
}
