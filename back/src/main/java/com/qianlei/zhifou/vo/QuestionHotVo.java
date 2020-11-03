package com.qianlei.zhifou.vo;

import com.qianlei.zhifou.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** @author qianlei */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionHotVo {
  private Question question;
  private Long hot;
}
