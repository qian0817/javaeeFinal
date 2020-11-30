package com.qianlei.zhifou.vo;

import com.qianlei.zhifou.pojo.es.Answer;
import com.qianlei.zhifou.pojo.es.Question;
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
