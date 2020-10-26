package com.qianlei.zhifou.vo;

import com.qianlei.zhifou.entity.Answer;
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

  public AnswerVo(Answer answer, UserVo user) {
    setId(answer.getId());
    setQuestionId(answer.getQuestionId());
    setContent(answer.getContent());
    setCreateTime(answer.getCreateTime());
    setUpdateTime(answer.getUpdateTime());
    this.user = user;
  }
}
