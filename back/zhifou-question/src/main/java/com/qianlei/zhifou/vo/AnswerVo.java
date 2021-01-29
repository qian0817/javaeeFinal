package com.qianlei.zhifou.vo;

import com.qianlei.zhifou.po.Answer;
import com.qianlei.zhifou.po.Question;
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

  public AnswerVo(
      Answer answer, UserVo user, QuestionVo question, Boolean canAgree, Long agreeNumber) {
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

  public Answer toAnswer() {
    Answer answer = new Answer();
    answer.setId(id);
    answer.setUserId(user.getId());
    answer.setQuestionId(questionId);
    answer.setContent(content);
    answer.setCreateTime(createTime);
    answer.setUpdateTime(updateTime);
    return answer;
  }
}
