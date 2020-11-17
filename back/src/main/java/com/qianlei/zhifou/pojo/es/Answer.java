package com.qianlei.zhifou.pojo.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

/** @author qianlei */
@Document(indexName = "zhifou_answer")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Answer {
  @Id private Integer id;

  @Field(name = "user_id")
  private Integer userId;

  @Field(name = "question_id")
  private Integer questionId;

  @Field(
      name = "content",
      type = FieldType.Text,
      analyzer = "ik_max_word",
      searchAnalyzer = "ik_smart")
  private String content;

  @Field(name = "create_time", type = FieldType.Date, format = DateFormat.basic_date_time)
  private LocalDateTime createTime;

  @Field(name = "update_time", type = FieldType.Date, format = DateFormat.basic_date_time)
  private LocalDateTime updateTime;
}
