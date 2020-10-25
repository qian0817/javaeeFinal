package com.qianlei.zhifou.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author qianlei
 */
@Table("zhifou_question")
@Data
public class Question {
    @Id
    private Integer id;

    @Column("title")
    private String title;

    @Column("tags")
    private String tags;

    @Column("content")
    private String content;
}
