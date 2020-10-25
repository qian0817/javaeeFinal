package com.qianlei.zhifou.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * @author qianlei
 */
@Table("zhifou_answer")
@Data
public class Answer {
    @Id
    private Integer id;
    @Column("user_id")
    private String userId;
    @Column("content")
    private String content;
    @Column("create_time")
    private LocalDateTime createTime;
    @Column("update_time")
    private LocalDateTime updateTime;
}
