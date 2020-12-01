package com.qianlei.zhifou.vo;

import com.qianlei.zhifou.pojo.es.QuestionEs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

/** @author qianlei */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDetailVo {
  private QuestionEs questionEs;
  private Page<AnswerVo> answers;
}
