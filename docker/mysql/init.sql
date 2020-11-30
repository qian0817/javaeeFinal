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
) COMMENT '用户表';

CREATE TABLE IF NOT EXISTS zhifou_follow
(
    id                BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    follower_user_id  INT    NOT NULL COMMENT '被关注者用户 id',
    following_user_id INT    NOT NULL COMMENT '关注者用户 id'
);

CREATE TABLE zhifou_user_event
(
    id          BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT PRIMARY KEY,
    user_id     INT         NOT NULL,
    operation   VARCHAR(20) NOT NULL,
    table_name  VARCHAR(20) NOT NULL,
    table_id    VARCHAR(50) NOT NULL,
    create_time TIMESTAMP   NOT NULL
) COMMENT '用户动态表';

CREATE TABLE zhifou_feed
(
    id             BIGINT    NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id        INT       NOT NULL,
    create_user_id INT       NOT NULL,
    event_id       BIGINT    NOT NULL,
    create_time    TIMESTAMP NOT NULL
) COMMENT 'feed 表';