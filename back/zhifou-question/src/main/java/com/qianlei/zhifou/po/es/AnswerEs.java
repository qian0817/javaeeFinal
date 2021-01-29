package com.qianlei.zhifou.po.es;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/** @author qianlei */
@Document(indexName = "zhifou_answer")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnswerEs {
  @Id private Integer id;

  @Field(
      name = "content",
      type = FieldType.Text,
      analyzer = "ik_max_word",
      searchAnalyzer = "ik_smart")
  private String content;
}
