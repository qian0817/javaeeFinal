package com.qianlei.zhifou.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

/**
 * @author qianlei
 */
@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {
  @ExceptionHandler(ZhiFouException.class)
  public Mono<BaseResponse<?>> handleZhiFouException(ZhiFouException e) {
    return Mono.just(new BaseResponse<>(-1, e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public Mono<BaseResponse<?>> handleZhiFouException(Exception e) {
    log.error("未知错误", e);
    return Mono.just(new BaseResponse<>(1, "未知错误"));
  }
}
