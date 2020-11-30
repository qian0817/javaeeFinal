package com.qianlei.zhifou.service.impl;

import cn.hutool.http.HtmlUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianlei.zhifou.common.Constant;
import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.AgreeDao;
import com.qianlei.zhifou.dao.es.AnswerDao;
import com.qianlei.zhifou.dao.es.QuestionDao;
import com.qianlei.zhifou.pojo.Agree;
import com.qianlei.zhifou.pojo.UserEvent;
import com.qianlei.zhifou.pojo.es.Answer;
import com.qianlei.zhifou.service.IAnswerService;
import com.qianlei.zhifou.service.IQuestionService;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.vo.AnswerVo;
import com.qianlei.zhifou.vo.UserVo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.qianlei.zhifou.common.Constant.KafkaConstant.DELETE_USER_EVENT_TOPIC;
import static com.qianlei.zhifou.common.Constant.UserEventConstant.DynamicAction.AGREE_ANSWER;
import static com.qianlei.zhifou.common.Constant.UserEventConstant.DynamicAction.CREATE_ANSWER;
import static com.qianlei.zhifou.common.Constant.UserEventConstant.TABLE_NAME_ANSWER;

/** @author qianlei */
@Service
@Slf4j
@Transactional(rollbackFor = RuntimeException.class)
public class AnswerServiceImpl implements IAnswerService {
  @Autowired private AnswerDao answerDao;
  @Autowired private QuestionDao questionDao;
  @Autowired private IQuestionService questionService;
  @Autowired private IUserService userService;
  @Autowired private AgreeDao agreeDao;
  @Autowired private ObjectMapper objectMapper;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  private static final List<String> SUPPORTED_SORT_BY_PROPERTIES =
      List.of("createTime", "updateTime");
  private static final List<String> SUPPORT_SORT_DIRECTION = List.of("asc", "desc");

  @SneakyThrows
  @Override
  public Answer createAnswer(Answer answer, UserVo user) {
    // XSS 过滤
    answer.setContent(Jsoup.clean(answer.getContent(), Whitelist.relaxed()));
    if (StringUtils.isBlank(HtmlUtil.cleanHtmlTag(answer.getContent()))) {
      throw new ZhiFouException("回答不能为空");
    }
    if (!questionDao.existsById(answer.getQuestionId())) {
      throw new ZhiFouException("问题不存在");
    }
    if (answerDao.findByUserIdAndQuestionId(user.getId(), answer.getQuestionId()) != null) {
      throw new ZhiFouException("已回答该问题");
    }
    answer.setId(null);
    answer.setUserId(user.getId());
    answer.setUpdateTime(LocalDateTime.now());
    answer.setCreateTime(LocalDateTime.now());
    answerDao.save(answer);
    // 向消息队列中发送相关的消息
    var userEvent =
        new UserEvent(
            null,
            user.getId(),
            CREATE_ANSWER.getId(),
            TABLE_NAME_ANSWER,
            answer.getId(),
            answer.getCreateTime());
    kafkaTemplate.send(
        Constant.KafkaConstant.ADD_USER_EVENT_TOPIC, objectMapper.writeValueAsString(userEvent));
    questionService.improveQuestionHeatLevel(answer.getQuestionId(), 100);
    return answer;
  }

  @Override
  public AnswerVo getAnswerByQuestionId(String answerId, @Nullable UserVo user) {
    var answer = answerDao.findById(answerId).orElseThrow(() -> new ZhiFouException("问题不存在"));
    // 回答者用户信息
    var answerUser = userService.getUserInfoByUserId(answer.getUserId());
    var question = questionDao.findById(answer.getQuestionId()).orElseThrow();
    long agreeNumber = agreeDao.countByAnswerId(answer.getId());
    questionService.improveQuestionHeatLevel(answer.getQuestionId(), 1);
    if (user == null) {
      return new AnswerVo(answer, answerUser, question, true, agreeNumber);
    } else {
      boolean canAgree = !agreeDao.existsByAnswerIdAndUserId(answerId, user.getId());
      return new AnswerVo(answer, answerUser, question, canAgree, agreeNumber);
    }
  }

  @SneakyThrows
  @Override
  public void agree(String answerId, UserVo user) {
    if (!answerDao.existsById(answerId)) {
      throw new ZhiFouException("回答不存在");
    }
    if (agreeDao.existsByAnswerIdAndUserId(answerId, user.getId())) {
      throw new ZhiFouException("不能重复点赞");
    }
    agreeDao.save(new Agree(null, user.getId(), answerId));

    // 向消息队列中发送相关的消息
    var userEvent =
        new UserEvent(
            null,
            user.getId(),
            AGREE_ANSWER.getId(),
            TABLE_NAME_ANSWER,
            answerId,
            LocalDateTime.now());
    kafkaTemplate.send(
        Constant.KafkaConstant.ADD_USER_EVENT_TOPIC, objectMapper.writeValueAsString(userEvent));
  }

  @SneakyThrows
  @Override
  public void deleteAgree(String answerId, UserVo user) {
    agreeDao.deleteByAnswerIdAndUserId(answerId, user.getId());
    // 向消息队列中发送相关的消息
    var userEvent =
        new UserEvent(null, user.getId(), AGREE_ANSWER.getId(), TABLE_NAME_ANSWER, answerId, null);
    kafkaTemplate.send(DELETE_USER_EVENT_TOPIC, objectMapper.writeValueAsString(userEvent));
  }

  @Override
  public Page<AnswerVo> getAllAnswerByQuestionId(
      String questionId,
      String sortDirection,
      String sortBy,
      int pageNum,
      int pageSize,
      UserVo user) {

    if (!SUPPORTED_SORT_BY_PROPERTIES.contains(sortBy)) {
      throw new ZhiFouException("不支持的排序类型" + sortBy);
    }
    if (!SUPPORT_SORT_DIRECTION.contains(sortDirection)) {
      throw new ZhiFouException("不支持的排序方向" + sortDirection);
    }
    return answerDao
        .findAllByQuestionId(
            questionId, PageRequest.of(pageNum, pageSize, Sort.by(Sort.Order.asc(sortBy))))
        .map(
            answer -> {
              var answerUser = userService.getUserInfoByUserId(answer.getUserId());
              var agreeNumber = agreeDao.countByAnswerId(answer.getId());
              if (user != null) {
                var canAgree = !agreeDao.existsByAnswerIdAndUserId(answer.getId(), user.getId());
                return new AnswerVo(answer, answerUser, null, canAgree, agreeNumber);
              } else {
                return new AnswerVo(answer, answerUser, null, true, agreeNumber);
              }
            });
  }
}
