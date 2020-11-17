package com.qianlei.zhifou.vo;

import com.qianlei.zhifou.pojo.es.Answer;
import com.qianlei.zhifou.pojo.es.Question;
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
  private Question question;

  public AnswerVo(
      Answer answer, UserVo user, Question question, Boolean canAgree, Long agreeNumber) {
    setId(answer.getId());
    setQuestionId(answer.getQuestionId());
    setContent(answer.getContent());
    setCreateTime(answer.getCreateTime());
    setUpdateTime(answer.getUpdateTime());
    setQuestion(question);
    setUser(user);
    setCanAgree(canAgree);
    setAgreeNumber(agreeNumber);
  }
}
