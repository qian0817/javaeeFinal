package com.qianlei.zhifou.vo;

import com.qianlei.zhifou.po.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** @author qianlei */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionHotVo {
  private Integer id;
  private String title;
  private String tags;
  private String content;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;
  private Long hot;

  public QuestionHotVo(Question question, Long hot) {
    setId(question.getId());
    setTitle(question.getTitle());
    setTags(question.getTags());
    setContent(question.getContent());
    setCreateTime(question.getCreateTime());
    setUpdateTime(question.getUpdateTime());
    setHot(hot);
  }
}
