package com.qianlei.zhifou.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** @author qianlei */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynamicWithUserVo {
  private Long eventId;
  private UserVo user;
  private String action;
  private Object content;
  private LocalDateTime createTime;

  public DynamicWithUserVo(DynamicVo dynamic,UserVo user){
    setUser(user);
    setEventId(dynamic.getEventId());
    setAction(dynamic.getAction());
    setContent(dynamic.getContent());
    setCreateTime(dynamic.getCreateTime());
  }
}
