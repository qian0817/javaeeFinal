package com.qianlei.zhifou.vo;

import com.qianlei.zhifou.entity.Answer;
import com.qianlei.zhifou.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** @author qianlei */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDetailVo {
  private Question question;
  private List<Answer> answers;
}
