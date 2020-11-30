package com.qianlei.zhifou.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianlei.zhifou.dao.FeedDao;
import com.qianlei.zhifou.dao.FollowDao;
import com.qianlei.zhifou.dao.UserEventDao;
import com.qianlei.zhifou.dao.es.AnswerDao;
import com.qianlei.zhifou.dao.es.QuestionDao;
import com.qianlei.zhifou.pojo.Feed;
import com.qianlei.zhifou.pojo.Follow;
import com.qianlei.zhifou.pojo.UserEvent;
import com.qianlei.zhifou.service.IAnswerService;
import com.qianlei.zhifou.service.IDynamicService;
import com.qianlei.zhifou.service.IQuestionService;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.vo.AnswerWithQuestionVo;
import com.qianlei.zhifou.vo.DynamicVo;
import com.qianlei.zhifou.vo.DynamicWithUserVo;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.qianlei.zhifou.common.Constant.KafkaConstant.*;
import static com.qianlei.zhifou.common.Constant.UserEventConstant.TABLE_NAME_ANSWER;
import static com.qianlei.zhifou.common.Constant.UserEventConstant.TABLE_NAME_QUESTION;

/** @author qianlei */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class DynamicServiceImpl implements IDynamicService {
  @Autowired private FeedDao feedDao;
  @Autowired private UserEventDao userEventDao;
  @Autowired private IAnswerService answerService;
  @Autowired private IQuestionService questionService;
  @Autowired private AnswerDao answerDao;
  @Autowired private QuestionDao questionDao;
  @Autowired private FollowDao followDao;
  @Autowired private IUserService userService;
  @Autowired private ObjectMapper mapper;

  @KafkaListener(topics = ADD_USER_EVENT_TOPIC, groupId = "add-dynamic")
  public void addDynamic(ConsumerRecord<String, String> record) throws JsonProcessingException {
    String value = record.value();
    var userEvent = mapper.readValue(value, UserEvent.class);
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
    var userEvent = mapper.readValue(value, UserEvent.class);
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
    var follow = mapper.readValue(value, Follow.class);
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
    var follow = mapper.readValue(value, Follow.class);
    feedDao.deleteAllByCreateUserIdAndUserId(
        follow.getFollowerUserId(), follow.getFollowingUserId());
  }

  @Override
  public Page<DynamicVo> getDynamicByUser(Integer userId, Integer pageNum, Integer pageSize) {
    var userEvents =
        userEventDao.findAllByUserId(
            userId, PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "createTime")));
    return userEvents.map(this::getDynamicByEvent);
  }

  @Override
  public Page<DynamicWithUserVo> getDynamicVoUserFollowing(
      Integer userId, Integer pageNum, Integer pageSize) {
    var feeds =
        feedDao.findAllByUserId(
            userId, PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "createTime")));
    return feeds
        .map(
            feed -> {
              var event = userEventDao.findById(feed.getEventId());
              if (event.isEmpty()) {
                return null;
              }
              return getDynamicByEvent(event.get());
            })
        .map(
            dynamicVo -> {
              var user = userService.getUserInfoByUserId(dynamicVo.getUserId());
              return new DynamicWithUserVo(dynamicVo, user);
            });
  }

  private DynamicVo getDynamicByEvent(UserEvent event) {
    var operation = event.action();
    Object content = null;
    switch (event.getTableName()) {
      case TABLE_NAME_ANSWER:
        var answer = answerDao.findById(event.getTableId()).orElseThrow();
        var question = questionDao.findById(answer.getQuestionId()).orElseThrow();
        content = new AnswerWithQuestionVo(answer, question);
        break;
      case TABLE_NAME_QUESTION:
        content = questionDao.findById(event.getTableId()).orElse(null);
        break;
      default:
    }
    return new DynamicVo(
        event.getId(), event.getUserId(), operation.getDesc(), content, event.getCreateTime());
  }
}
