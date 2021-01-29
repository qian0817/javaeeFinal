package com.qianlei.zhifou.vo;

import com.qianlei.zhifou.po.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/** @author qianlei */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionVo {
  private Integer id;
  private String title;
  private String tags;
  private String content;
  private LocalDateTime createTime;
  private LocalDateTime updateTime;
  private Boolean canCreateAnswer;
  private Integer myAnswerId;

  public QuestionVo(Question question, Boolean canCreateAnswer, Integer myAnswerId) {
    setId(question.getId());
    setTitle(question.getTitle());
    setTags(question.getTags());
    setContent(question.getContent());
    setCreateTime(question.getCreateTime());
    setUpdateTime(question.getUpdateTime());
    setCanCreateAnswer(canCreateAnswer);
    setMyAnswerId(myAnswerId);
  }
}
