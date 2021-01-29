package com.qianlei.zhifou.requestparam;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** @author qianlei */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterParam {
  private String username;
  private String password;
  private String email;
  private String code;
}
