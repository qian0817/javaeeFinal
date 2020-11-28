package com.qianlei.zhifou.vo;

import com.qianlei.zhifou.pojo.es.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** @author qianlei */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionVo {
  private Question question;
  private boolean canCreateAnswer;
  private String myAnswerId;
}
