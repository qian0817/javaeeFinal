package com.qianlei.zhifou.filter;

import com.nimbusds.jose.jwk.RSAKey;
import com.qianlei.zhifou.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 从请求头中的 Authorization 字段获取用户信息
 *
 * @author qianlei
 */
@Component
@WebFilter(filterName = "jwtFilter", urlPatterns = "/*")
public class JwtFilter implements Filter {

  @Autowired private RSAKey rsaKey;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    var httpServletRequest = (HttpServletRequest) request;
    var authorization = httpServletRequest.getHeader("Authorization");
    if (StringUtils.startsWithIgnoreCase(authorization, "Bearer ")) {
      var user = JwtUtils.checkJwt(authorization.substring(7), rsaKey);
      if (user != null) {
        request.setAttribute("user", user);
      }
    }
    chain.doFilter(request, response);
  }
}
