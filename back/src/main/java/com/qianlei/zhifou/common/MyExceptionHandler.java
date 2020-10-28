package com.qianlei.zhifou.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** @author qianlei */
@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {
  @ExceptionHandler(ZhiFouException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public ErrorResponse handleZhiFouException(ZhiFouException e) {
    return new ErrorResponse(400, e.getMessage());
  }

  @ExceptionHandler(AuthorizationException.class)
  @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
  public ErrorResponse handleAuthorizationException(AuthorizationException e) {
    return new ErrorResponse(401, e.getMessage());
  }


  @ExceptionHandler(RuntimeException.class)
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleRunTimeException(RuntimeException e) {
    log.error("未知错误", e);
    return new ErrorResponse(500, "未知错误");
  }
}
