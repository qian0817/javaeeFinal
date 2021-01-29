package com.qianlei.zhifou.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** @author qianlei */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
  private Integer id;
  private String username;
  private String email;
}
