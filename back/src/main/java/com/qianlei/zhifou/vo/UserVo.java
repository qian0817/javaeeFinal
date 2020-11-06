package com.qianlei.zhifou.vo;

import com.qianlei.zhifou.pojo.User;
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

  public UserVo(User user) {
    setId(user.getId());
    setUsername(user.getUsername());
  }
}
