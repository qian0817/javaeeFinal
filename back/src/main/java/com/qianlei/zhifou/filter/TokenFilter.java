package com.qianlei.zhifou.filter;

import com.qianlei.zhifou.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** @author qianlei */
@Order
@Slf4j
@Component
@WebFilter(value = "tokenFilter", urlPatterns = "/**")
public class TokenFilter implements Filter {
  @Autowired private IUserService userService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    var httpRequest = (HttpServletRequest) request;
    var httpResponse = (HttpServletResponse) response;
    var cookies = httpRequest.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ("token".equals(cookie.getName())) {
          var newToken = userService.refreshToken(cookie.getValue());
          // 设置刷新后的 cookie
          Cookie newCookie = new Cookie("token", newToken.getToken());
          newCookie.setSecure(true);
          newCookie.setHttpOnly(true);
          newCookie.setMaxAge(3600 * 24 * 7);
          newCookie.setPath("/");
          httpResponse.addCookie(newCookie);
          break;
        }
      }
    }
    chain.doFilter(request, response);
  }
}
