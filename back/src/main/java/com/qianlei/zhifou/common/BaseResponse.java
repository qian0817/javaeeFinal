package com.qianlei.zhifou.common;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qianlei
 */
@Data
@NoArgsConstructor
public class BaseResponse<T> {
  private int id = 0;
  private T data;
  private String message = "";

  public BaseResponse(T data) {
    this.data = data;
  }

  public BaseResponse(int error, String message) {
    this.message = message;
    this.id = error;
  }
}
