package com.qianlei.zhifou.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author qianlei
 */
@Table("zhifou_comment")
@Data
public class Comment {
    @Id
    private Integer id;
    @Column("answer_id")
    private Integer answerId;
    @Column("content")
    private String content;
    @Column("user_id")
    private String userId;
}
