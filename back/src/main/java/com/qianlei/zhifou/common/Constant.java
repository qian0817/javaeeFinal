package com.qianlei.zhifou.common;

public interface Constant {
  interface KafkaConstant {
    // 发送用户注册验证码的 kafka topic
    String SEND_REGISTER_CODE_EMAIL_TOPIC = "topic-send-register-code-email";
  }

  interface RedisConstant {
    // 用于判断是否发送验证码
    String REGISTER_MAIL_SEND_PREFIX = "user:register:email:send:";
    // 发送的验证码信息
    String REGISTER_CODE_PREFIX = "user:register:email:code:";
  }
}
