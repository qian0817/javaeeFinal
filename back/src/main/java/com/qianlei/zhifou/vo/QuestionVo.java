package com.qianlei.zhifou.vo;

import com.qianlei.zhifou.pojo.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** @author qianlei */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionVo {
  private Question question;
  private Boolean canCreateAnswer;
  private Integer myAnswerId;
}
