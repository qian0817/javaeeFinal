package com.qianlei.zhifou.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.FollowDao;
import com.qianlei.zhifou.po.Follow;
import com.qianlei.zhifou.service.IFollowService;
import lombok.SneakyThrows;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

import static com.qianlei.zhifou.common.Constant.KafkaConstant.FOLLOW_USER_TOPIC;
import static com.qianlei.zhifou.common.Constant.KafkaConstant.UNFOLLOW_USER_TOPIC;

/**
 * @author qianlei
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class FollowServiceImpl implements IFollowService {
  @Resource private FollowDao followDao;
  @Resource private KafkaTemplate<String, String> kafkaTemplate;
  @Resource private ObjectMapper objectMapper;

  @SneakyThrows
  @Override
  public void follow(Integer followerUserId, Integer followingUserId) {
    if (Objects.equals(followerUserId, followingUserId)) {
      throw new ZhiFouException("不能关注自己");
    }
    if (followDao.existsByFollowerUserIdAndFollowingUserId(followerUserId, followingUserId)) {
      throw new ZhiFouException("已关注该用户");
    }
    var follow = new Follow(null, followerUserId, followingUserId);
    followDao.save(follow);
    kafkaTemplate.send(FOLLOW_USER_TOPIC, objectMapper.writeValueAsString(follow));
  }

  @SneakyThrows
  @Override
  public void unfollow(Integer unfollowerUserId, Integer unfollowingUserId) {
    if (!followDao.existsByFollowerUserIdAndFollowingUserId(unfollowerUserId, unfollowingUserId)) {
      throw new ZhiFouException("未关注该用户");
    }
    followDao.deleteAllByFollowerUserIdAndFollowingUserId(unfollowerUserId, unfollowingUserId);
    var follow = new Follow(null, unfollowerUserId, unfollowingUserId);
    kafkaTemplate.send(UNFOLLOW_USER_TOPIC, objectMapper.writeValueAsString(follow));
  }

  @Override
  public Long countFollower(Integer userId) {
    return followDao.countByFollowerUserId(userId);
  }

  @Override
  public Long countFollowing(Integer userId) {
    return followDao.countByFollowingUserId(userId);
  }

  @Override
  public Boolean existByFollowerAndFollowing(Integer followerUserId, Integer followingUserId) {
    return followDao.existsByFollowerUserIdAndFollowingUserId(followerUserId,followingUserId);
  }
}
