package com.qianlei.zhifou.common;

import lombok.Data;
import lombok.NoArgsConstructor;

/** @author qianlei */
@Data
@NoArgsConstructor
public class ErrorResponse {
  private int id = 0;
  private String message = "";

  public ErrorResponse(int error, String message) {
    this.message = message;
    this.id = error;
  }
}
