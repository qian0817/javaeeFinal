package com.qianlei.zhifou.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** @author qianlei */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DynamicVo {
  private Long eventId;
  private Integer userId;
  private String action;
  private Object content;
  private LocalDateTime createTime;
}
