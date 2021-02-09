package com.qianlei.zhifou.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** @author qianlei */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionVo {
  private Integer id;
  private String title;
  private String tags;
  private String content;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;
  private Boolean canCreateAnswer;
  private Integer myAnswerId;
}
