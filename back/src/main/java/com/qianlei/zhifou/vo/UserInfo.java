package com.qianlei.zhifou.vo;

import com.qianlei.zhifou.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** @author qianlei */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
  private Integer id;
  /** 用户名 */
  private String username;
  /** 是否可以被关注 */
  private Boolean following;
  /** 总回答数 */
  private Long totalAnswer;
  /** 总赞同数 */
  private Long totalAgree;
  /** 总共关注了多少人 */
  private Long totalFollowing;
  /** 总共被多少人关注 */
  private Long totalFollower;

  public UserInfo(User user) {
    setId(user.getId());
    setUsername(user.getUsername());
  }
}
