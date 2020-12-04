package com.qianlei.zhifou.requestparam;

import com.qianlei.zhifou.pojo.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** @author qianlei */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateQuestionParam {
  private String title;
  private String tags;
  private String content;

  public Question toQuestion() {
    var question = new Question();
    question.setContent(content);
    question.setTitle(title);
    question.setTags(tags);
    return question;
  }
}
