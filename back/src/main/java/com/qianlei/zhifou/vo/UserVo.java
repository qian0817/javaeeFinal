package com.qianlei.zhifou.vo;

import cn.authing.core.types.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** @author qianlei */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
  private String id;
  private String username;

  public UserVo(User user) {
    setId(user.getId());
    setUsername(user.getUsername());
  }
}
