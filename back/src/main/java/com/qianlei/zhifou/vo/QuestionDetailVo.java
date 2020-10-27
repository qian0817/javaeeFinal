package com.qianlei.zhifou.vo;

import com.qianlei.zhifou.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

/** @author qianlei */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDetailVo {
  private Question question;
  private Page<AnswerVo> answers;
}
