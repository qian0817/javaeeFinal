package com.qianlei.zhifou.service.impl;

import cn.hutool.core.lang.Validator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.UserDao;
import com.qianlei.zhifou.po.User;
import com.qianlei.zhifou.requestparam.RegisterParam;
import com.qianlei.zhifou.requestparam.UserLoginParam;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.vo.UserVo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.qianlei.zhifou.common.Constant.KafkaConstant.SEND_REGISTER_CODE_EMAIL_TOPIC;
import static com.qianlei.zhifou.common.Constant.RedisConstant.*;

/** @author qianlei */
@Service
@Slf4j
@Transactional(rollbackOn = RuntimeException.class)
public class UserServiceImpl implements IUserService {
  @Resource private UserDao userDao;
  @Resource private StringRedisTemplate stringRedisTemplate;
  @Resource private KafkaTemplate<String, String> kafkaTemplate;
  @Resource private PasswordEncoder passwordEncoder;
  @Resource private JavaMailSender mailSender;
  @Resource private ObjectMapper objectMapper;

  @Value("${spring.mail.username}")
  private String emailFrom;

  @SneakyThrows
  @Override
  public UserVo getUserInfoByUserId(Integer userId) {
    String cacheUser = stringRedisTemplate.opsForValue().get(USER_CACHE_REDIS_PREFIX + userId);
    if (cacheUser == null) {
      var user = userDao.findById(userId).orElseThrow(() -> new ZhiFouException("该用户不存在"));
      UserVo userVo = new UserVo(user);
      // 添加到缓存中
      stringRedisTemplate
          .opsForValue()
          .set(
              USER_CACHE_REDIS_PREFIX + userId,
              objectMapper.writeValueAsString(userVo),
              1,
              TimeUnit.HOURS);
      return userVo;
    } else {
      // 直接从缓存中返回
      return objectMapper.readValue(cacheUser, UserVo.class);
    }
  }

  @Override
  public User register(RegisterParam param) {
    if (StringUtils.isBlank(param.getUsername())) {
      throw new ZhiFouException("用户名不能为空");
    }
    if (StringUtils.isBlank(param.getPassword())) {
      throw new ZhiFouException("密码不能为空");
    }
    if (!Validator.isEmail(param.getEmail())) {
      throw new ZhiFouException("邮箱格式错误");
    }
    if (userDao.findByUsername(param.getUsername()).isPresent()) {
      throw new ZhiFouException("该用户名已经注册");
    }
    if (userDao.findByEmail(param.getEmail()).isPresent()) {
      throw new ZhiFouException("该邮箱已经被注册");
    }
    // 从 redis 中获取发送的验证码
    String code = stringRedisTemplate.opsForValue().get(REGISTER_CODE_PREFIX + param.getEmail());
    if (code == null) {
      throw new ZhiFouException("未发送验证码");
    }
    if (!code.equalsIgnoreCase(param.getCode())) {
      throw new ZhiFouException("邮箱验证码错误");
    }
    var encryptPassword = passwordEncoder.encode(param.getPassword());
    var user = new User(null, param.getUsername(), encryptPassword, param.getEmail());
    userDao.save(user);
    return user;
  }

  @Override
  public void sendRegisterEmail(String email) {
    if (!Validator.isEmail(email)) {
      throw new ZhiFouException("邮箱格式错误");
    }
    if (userDao.findByEmail(email).isPresent()) {
      throw new ZhiFouException("该邮箱已经被注册");
    }
    // 判断验证码在之前是否已经发送 设置过期时间为 1 分钟
    var isNotSend =
        stringRedisTemplate
            .opsForValue()
            .setIfAbsent(REGISTER_MAIL_SEND_PREFIX + email, email, 1, TimeUnit.MINUTES);
    if (isNotSend == null || !isNotSend) {
      throw new ZhiFouException("验证码已经发送");
    }
    kafkaTemplate.send(SEND_REGISTER_CODE_EMAIL_TOPIC, email);
  }

  @KafkaListener(topics = SEND_REGISTER_CODE_EMAIL_TOPIC, groupId = "send-email")
  public void sendRegisterEmail(ConsumerRecord<String, String> record) {
    var email = record.value();
    // 发送邮件
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(emailFrom);
    message.setTo(email);
    message.setSubject("知否注册验证");
    Random random = new Random();
    int code = 0;
    int codeLength = 6;
    for (int i = 0; i < codeLength; i++) {
      code = code * 10 + random.nextInt(10);
    }
    message.setText("您的验证码是" + String.format("%4d", code) + "\n有效时间为 5 分钟");
    mailSender.send(message);
    // 将其验证码保存在 redis 中，设置 5 分钟的过期时间
    stringRedisTemplate
        .opsForValue()
        .set(REGISTER_CODE_PREFIX + email, String.valueOf(code), 5, TimeUnit.MINUTES);
  }

  @Override
  public User login(UserLoginParam param) {
    var existedUser = userDao.findByUsername(param.getUsername());
    if (existedUser.isEmpty()) {
      throw new ZhiFouException("用户名或密码错误");
    }
    if (!passwordEncoder.matches(param.getPassword(), existedUser.get().getPassword())) {
      throw new ZhiFouException("用户名或密码错误");
    }
    return existedUser.get();
  }
}
