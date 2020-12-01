package com.qianlei.zhifou.vo;

import com.qianlei.zhifou.pojo.Answer;
import com.qianlei.zhifou.pojo.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** @author qianlei */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerWithQuestionVo {
  private Answer answer;
  private Question question;
}
