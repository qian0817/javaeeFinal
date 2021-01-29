package com.qianlei.zhifou.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** @author qianlei */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerVo {
  private Integer id;
  private UserVo user;
  private Integer questionId;
  private String content;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;
  private Boolean canAgree;
  private Long agreeNumber;
  private QuestionVo question;
}
