package com.qianlei.zhifou.requestparam;

import com.qianlei.zhifou.po.Answer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** @author qianlei */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateAnswerParam {
  private Integer questionId;
  private String content;

  public Answer toAnswer(int userId) {
    var answer = new Answer();
    answer.setContent(content);
    answer.setQuestionId(questionId);
    answer.setUserId(userId);
    return answer;
  }
}
