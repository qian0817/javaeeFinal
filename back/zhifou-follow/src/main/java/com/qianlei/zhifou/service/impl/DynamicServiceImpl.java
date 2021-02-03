package com.qianlei.zhifou.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianlei.zhifou.client.AnswerClient;
import com.qianlei.zhifou.client.QuestionClient;
import com.qianlei.zhifou.client.UserClient;
import com.qianlei.zhifou.dao.FeedDao;
import com.qianlei.zhifou.dao.FollowDao;
import com.qianlei.zhifou.dao.UserEventDao;
import com.qianlei.zhifou.po.Feed;
import com.qianlei.zhifou.po.Follow;
import com.qianlei.zhifou.po.UserEvent;
import com.qianlei.zhifou.service.IDynamicService;
import com.qianlei.zhifou.vo.DynamicVo;
import com.qianlei.zhifou.vo.DynamicWithUserVo;
import com.qianlei.zhifou.vo.UserVo;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static com.qianlei.zhifou.common.Constant.KafkaConstant.*;
import static com.qianlei.zhifou.common.Constant.UserEventConstant.TABLE_NAME_ANSWER;
import static com.qianlei.zhifou.common.Constant.UserEventConstant.TABLE_NAME_QUESTION;

/** @author qianlei */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class DynamicServiceImpl implements IDynamicService {
  @Resource private FeedDao feedDao;
  @Resource private UserEventDao userEventDao;
  @Resource private AnswerClient answerClient;
  @Resource private QuestionClient questionClient;
  @Resource private FollowDao followDao;
  @Autowired private UserClient userClient;

  @Resource private ObjectMapper objectMapper;

  @KafkaListener(topics = ADD_USER_EVENT_TOPIC, groupId = "add-dynamic")
  public void addDynamic(ConsumerRecord<String, String> record) throws JsonProcessingException {
    String value = record.value();
    var userEvent = objectMapper.readValue(value, UserEvent.class);
    userEventDao.save(userEvent);
    var followers = followDao.findAllByFollowerUserId(userEvent.getUserId());
    followers.forEach(
        follow ->
            feedDao.save(
                new Feed(
                    null,
                    follow.getFollowingUserId(),
                    userEvent.getUserId(),
                    userEvent.getId(),
                    userEvent.getCreateTime())));
  }

  @KafkaListener(topics = DELETE_USER_EVENT_TOPIC, groupId = "delete-dynamic")
  public void deleteDynamic(ConsumerRecord<String, String> record) throws JsonProcessingException {
    String value = record.value();
    var userEvent = objectMapper.readValue(value, UserEvent.class);
    userEvent =
        userEventDao.findByUserIdAndOperationAndTableNameAndTableId(
            userEvent.getUserId(),
            userEvent.getOperation(),
            userEvent.getTableName(),
            userEvent.getTableId());
    feedDao.deleteAllByEventId(userEvent.getId());
    userEventDao.deleteById(userEvent.getId());
  }

  @KafkaListener(topics = FOLLOW_USER_TOPIC, groupId = "add-feed")
  public void addFeed(ConsumerRecord<String, String> record) throws JsonProcessingException {
    String value = record.value();
    var follow = objectMapper.readValue(value, Follow.class);
    var userEvents = userEventDao.findAllByUserId(follow.getFollowerUserId());
    userEvents.forEach(
        event ->
            feedDao.save(
                new Feed(
                    null,
                    follow.getFollowingUserId(),
                    follow.getFollowerUserId(),
                    event.getId(),
                    event.getCreateTime())));
  }

  @KafkaListener(topics = UNFOLLOW_USER_TOPIC, groupId = "delete-feed")
  public void deleteFeed(ConsumerRecord<String, String> record) throws JsonProcessingException {
    String value = record.value();
    var follow = objectMapper.readValue(value, Follow.class);
    feedDao.deleteAllByCreateUserIdAndUserId(
        follow.getFollowerUserId(), follow.getFollowingUserId());
  }

  @Override
  public Page<DynamicVo> getDynamicByUser(
      Integer userId, UserVo user, Integer pageNum, Integer pageSize) {
    var userEvents =
        userEventDao.findAllByUserId(
            userId, PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "createTime")));
    return userEvents.map(event -> getDynamicByEvent(event, user));
  }

  @Override
  public Page<DynamicWithUserVo> getDynamicVoUserFollowing(
      UserVo user, Integer pageNum, Integer pageSize) {
    var feeds =
        feedDao.findAllByUserId(
            user.getId(),
            PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "createTime")));
    return feeds
        .map(
            feed -> {
              var event = userEventDao.findById(feed.getEventId()).orElseThrow();
              return getDynamicByEvent(event, user);
            })
        .map(
            dynamicVo -> {
              var createUser = userClient.getUserById(dynamicVo.getUserId());
              return new DynamicWithUserVo(dynamicVo, createUser);
            });
  }

  private DynamicVo getDynamicByEvent(UserEvent event, @Nullable UserVo user) {
    var operation = event.action();
    Object content = null;
    switch (event.getTableName()) {
      case TABLE_NAME_ANSWER:
        content = answerClient.getAnswerById(event.getTableId());
        break;
      case TABLE_NAME_QUESTION:
        content = questionClient.getQuestionById(event.getTableId());
        break;
      default:
    }
    return new DynamicVo(
        event.getId(), event.getUserId(), operation.getDesc(), content, event.getCreateTime());
  }
}
