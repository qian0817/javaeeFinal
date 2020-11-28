package com.qianlei.zhifou.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.crypto.digest.BCrypt;
import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.AgreeDao;
import com.qianlei.zhifou.dao.FollowDao;
import com.qianlei.zhifou.dao.UserDao;
import com.qianlei.zhifou.dao.es.AnswerDao;
import com.qianlei.zhifou.pojo.Follow;
import com.qianlei.zhifou.pojo.User;
import com.qianlei.zhifou.requestparam.RegisterParam;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.vo.UserInfo;
import com.qianlei.zhifou.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.qianlei.zhifou.common.Constant.KafkaConstant.SEND_REGISTER_CODE_EMAIL_TOPIC;
import static com.qianlei.zhifou.common.Constant.RedisConstant.REGISTER_CODE_PREFIX;
import static com.qianlei.zhifou.common.Constant.RedisConstant.REGISTER_MAIL_SEND_PREFIX;

/** @author qianlei */
@Service
@Slf4j
@Transactional(rollbackOn = RuntimeException.class)
public class UserServiceImpl implements IUserService {

  @Autowired private FollowDao followDao;
  @Autowired private UserDao userDao;
  @Autowired private AgreeDao agreeDao;
  @Autowired private AnswerDao answerDao;
  @Autowired private StringRedisTemplate redisTemplate;

  @Value("${spring.mail.username}")
  private String emailFrom;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private JavaMailSender mailSender;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Override
  public UserVo getUserInfoByUserId(Integer userId) {
    var user = userDao.findById(userId).orElseThrow(() -> new ZhiFouException("该用户不存在"));
    return new UserVo(user);
  }

  @Override
  public UserInfo getUserInfoByUserId(Integer userId, @Nullable UserVo user) {
    var findUser = userDao.findById(userId).orElseThrow(() -> new ZhiFouException("该用户不存在"));
    var totalAgree = agreeDao.countByUserId(findUser.getId());
    var totalAnswer = answerDao.countByUserId(findUser.getId());
    var totalFollower = followDao.countByFollowerUserId(userId);
    var totalFollowing = followDao.countByFollowingUserId(userId);
    var canFollow =
        user == null || followDao.existsByFollowerUserIdAndFollowingUserId(userId, user.getId());
    var isMe = user != null && user.getId().equals(userId);
    return new UserInfo(
        findUser.getId(),
        findUser.getUsername(),
        canFollow,
        isMe,
        totalAnswer,
        totalAgree,
        totalFollowing,
        totalFollower);
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
    String code = redisTemplate.opsForValue().get(REGISTER_CODE_PREFIX + param.getEmail());
    if (code == null) {
      throw new ZhiFouException("未发送验证码");
    }
    if (!code.equalsIgnoreCase(param.getCode())) {
      throw new ZhiFouException("邮箱验证码错误");
    }
    var encryptPassword = BCrypt.hashpw(param.getPassword());
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
        redisTemplate
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
    redisTemplate
        .opsForValue()
        .set(REGISTER_CODE_PREFIX + email, String.valueOf(code), 5, TimeUnit.MINUTES);
  }

  @Override
  public User login(User user) {
    var existedUser = userDao.findByUsername(user.getUsername());
    if (existedUser.isEmpty()) {
      throw new ZhiFouException("用户名或密码错误");
    }
    if (!BCrypt.checkpw(user.getPassword(), existedUser.get().getPassword())) {
      throw new ZhiFouException("用户名或密码错误");
    }
    return existedUser.get();
  }

  @Override
  public void follow(Integer followerUserId, Integer followingUserId) {
    if (Objects.equals(followerUserId, followingUserId)) {
      throw new ZhiFouException("不能关注自己");
    }
    if (followDao.existsByFollowerUserIdAndFollowingUserId(followerUserId, followingUserId)) {
      throw new ZhiFouException("已关注该用户");
    }
    followDao.save(new Follow(null, followerUserId, followingUserId));
  }

  @Override
  public void unfollow(Integer unfollowerUserId, Integer unfollowingUserId) {
    followDao.deleteAllByFollowerUserIdAndFollowingUserId(unfollowerUserId, unfollowingUserId);
  }
}
