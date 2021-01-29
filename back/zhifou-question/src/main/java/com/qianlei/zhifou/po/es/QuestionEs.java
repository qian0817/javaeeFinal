package com.qianlei.zhifou.po.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/** @author qianlei */
@Document(indexName = "zhifou_question")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionEs {
  @Id private Integer id;

  @Field(
      value = "title",
      type = FieldType.Text,
      analyzer = "ik_max_word",
      searchAnalyzer = "ik_smart")
  private String title;

  @Field(
      value = "content",
      type = FieldType.Text,
      analyzer = "ik_max_word",
      searchAnalyzer = "ik_smart")
  private String content;
}
