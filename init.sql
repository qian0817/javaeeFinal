CREATE DATABASE IF NOT EXISTS zhifou;
USE zhifou;

CREATE TABLE IF NOT EXISTS zhifou_question
(
    id      INT         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    title   VARCHAR(50) NOT NULL COMMENT '标题',
    tags    VARCHAR(50) NOT NULL DEFAULT '' COMMENT '标签',
    content TEXT        NOT NULL DEFAULT '' COMMENT '内容'
) COMMENT '问题表';

CREATE TABLE IF NOT EXISTS zhifou_answer
(
    id          INT        NOT NULL PRIMARY KEY AUTO_INCREMENT,
    question_id INT        NOT NULL COMMENT '问题id',
    user_id     CHAR(24)   NOT NULL COMMENT '回答者用户id',
    content     MEDIUMTEXT NOT NULL COMMENT '回答正文',
    create_time TIMESTAMP  NOT NULL DEFAULT NOW() COMMENT '创建时间',
    update_time TIMESTAMP  NOT NULL ON UPDATE NOW() COMMENT '更新时间'
) COMMENT '回答表';


CREATE TABLE IF NOT EXISTS zhifou_agree
(
    id        INT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id   CHAR(24) NOT NULL COMMENT '回答者用户id',
    answer_id INT      NOT NULL
) COMMENT '赞同信息表';

CREATE TABLE IF NOT EXISTS zhifou_comment
(
    id          INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    answer_id   INT          NOT NULL,
    content     VARCHAR(100) NOT NULL,
    user_id     CHAR(24)     NOT NULL COMMENT '回答者用户id',
    create_time TIMESTAMP    NOT NULL DEFAULT NOW() COMMENT '创建时间'
) COMMENT '评论信息表';


