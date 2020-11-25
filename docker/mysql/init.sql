CREATE DATABASE IF NOT EXISTS zhifou;
USE zhifou;

CREATE TABLE IF NOT EXISTS zhifou_agree
(
    id        INT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id   INT      NOT NULL COMMENT '回答者用户id',
    answer_id CHAR(20) NOT NULL
) COMMENT '赞同信息表';

CREATE TABLE IF NOT EXISTS zhifou_comment
(
    id          INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    answer_id   CHAR(20)     NOT NULL,
    content     VARCHAR(100) NOT NULL,
    user_id     INT          NOT NULL COMMENT '回答者用户id',
    create_time TIMESTAMP    NOT NULL DEFAULT NOW() COMMENT '创建时间'
) COMMENT '评论信息表';

CREATE TABLE IF NOT EXISTS zhifou_user
(
    id       INT         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(20) NOT NULL,
    password CHAR(64)    NOT NULL,
    email    VARCHAR(30) NOT NULL
)


