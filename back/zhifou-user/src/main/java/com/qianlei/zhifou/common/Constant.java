package com.qianlei.zhifou.common;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

public interface Constant {
  interface KafkaConstant {
    // 发送用户注册验证码的 kafka topic
    String SEND_REGISTER_CODE_EMAIL_TOPIC = "topic-send-register-code-email";
    String ADD_USER_EVENT_TOPIC = "topic-add-user-event";
    String DELETE_USER_EVENT_TOPIC = "topic-delete-user-event";
    String FOLLOW_USER_TOPIC = "topic-follow-user";
    String UNFOLLOW_USER_TOPIC = "topic-unfollow-user";
  }

  interface RedisConstant {
    // 用于判断是否发送验证码
    String REGISTER_MAIL_SEND_PREFIX = "user:register:email:send:";
    // 发送的验证码信息
    String REGISTER_CODE_PREFIX = "user:register:email:code:";
    // 热榜排序信息
    String HOT_QUESTION_REDIS_PREFIX ="zhifou:question:hot:";
  }

  interface SecurityConstant {
    String TOKEN_HEADER = "Authorization";
    String TOKEN_PREFIX = "Bearer ";
  }

  interface UserEventConstant {

    String TABLE_NAME_ANSWER = "answer";
    String TABLE_NAME_QUESTION = "question";

    @AllArgsConstructor
    @Getter
    enum DynamicAction {
      /** 回答问题 */
      CREATE_ANSWER(1, "回答了问题"),
      /** 赞同问题 */
      AGREE_ANSWER(2, "赞同了回答");
      private final int id;
      @JsonValue private final String desc;
    }
  }

  interface HotQuestionConstant{
    String TIME_PATTERN ="yyyy:MM:dd:HH";
    DateTimeFormatter HOT_QUESTION_TIME_FORMATTER =DateTimeFormatter.ofPattern(TIME_PATTERN);
  }
}
