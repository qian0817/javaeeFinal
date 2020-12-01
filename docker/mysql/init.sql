CREATE DATABASE IF NOT EXISTS zhifou;
USE zhifou;

CREATE TABLE IF NOT EXISTS zhifou_agree
(
    id        INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id   INT NOT NULL COMMENT '回答者用户id',
    answer_id INT NOT NULL
) COMMENT '赞同信息表', CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS zhifou_question
(
    id          INT         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    title       VARCHAR(30) NOT NULL,
    tags        VARCHAR(30) NOT NULL,
    content     TEXT        NOT NULL,
    create_time TIMESTAMP   NOT NULL DEFAULT NOW() COMMENT '创建时间',
    update_time TIMESTAMP   NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '修改时间'
) COMMENT '问题信息表' , CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS zhifou_answer
(
    id          INT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id     INT       NOT NULL,
    question_id INT       NOT NULL,
    content     TEXT      NOT NULL,
    create_time TIMESTAMP NOT NULL DEFAULT NOW() COMMENT '创建时间',
    update_time TIMESTAMP NOT NULL DEFAULT NOW() ON UPDATE NOW() COMMENT '修改时间'
) COMMENT '回答信息表' , CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS zhifou_comment
(
    id          INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    answer_id   INT          NOT NULL,
    content     VARCHAR(100) NOT NULL,
    user_id     INT          NOT NULL COMMENT '回答者用户id',
    create_time TIMESTAMP    NOT NULL DEFAULT NOW() COMMENT '创建时间'
) COMMENT '评论信息表', CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS zhifou_user
(
    id       INT         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(20) NOT NULL,
    password CHAR(64)    NOT NULL,
    email    VARCHAR(30) NOT NULL
) COMMENT '用户表', CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS zhifou_follow
(
    id                BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    follower_user_id  INT    NOT NULL COMMENT '被关注者用户 id',
    following_user_id INT    NOT NULL COMMENT '关注者用户 id'
) COMMENT '用户关注表', CHARSET = utf8mb4;

CREATE TABLE zhifou_user_event
(
    id          BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT PRIMARY KEY,
    user_id     INT         NOT NULL,
    operation   VARCHAR(20) NOT NULL,
    table_name  VARCHAR(20) NOT NULL,
    table_id    VARCHAR(50) NOT NULL,
    create_time TIMESTAMP   NOT NULL
) COMMENT '用户动态表', CHARSET = utf8mb4;

CREATE TABLE zhifou_feed
(
    id             BIGINT    NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id        INT       NOT NULL,
    create_user_id INT       NOT NULL,
    event_id       BIGINT    NOT NULL,
    create_time    TIMESTAMP NOT NULL
) COMMENT 'feed 表', CHARSET = utf8mb4;