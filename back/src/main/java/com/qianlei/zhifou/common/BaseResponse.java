package com.qianlei.zhifou.common;

import lombok.Data;

/** @author qianlei */
@Data
public class BaseResponse<T> {
  private int id;
  private T data;
  private String message;
}
