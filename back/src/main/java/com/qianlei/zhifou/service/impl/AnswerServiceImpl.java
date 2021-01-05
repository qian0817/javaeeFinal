package com.qianlei.zhifou.service.impl;

import cn.hutool.http.HtmlUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianlei.zhifou.common.Constant;
import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.AgreeDao;
import com.qianlei.zhifou.dao.AnswerDao;
import com.qianlei.zhifou.dao.QuestionDao;
import com.qianlei.zhifou.dao.es.AnswerElasticsearchDao;
import com.qianlei.zhifou.pojo.Agree;
import com.qianlei.zhifou.pojo.Answer;
import com.qianlei.zhifou.pojo.UserEvent;
import com.qianlei.zhifou.pojo.es.AnswerEs;
import com.qianlei.zhifou.requestparam.CreateAnswerParam;
import com.qianlei.zhifou.service.IAnswerService;
import com.qianlei.zhifou.service.IQuestionService;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.utils.HtmlUtils;
import com.qianlei.zhifou.vo.AnswerVo;
import com.qianlei.zhifou.vo.UserVo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.qianlei.zhifou.common.Constant.KafkaConstant.DELETE_USER_EVENT_TOPIC;
import static com.qianlei.zhifou.common.Constant.UserEventConstant.DynamicAction.AGREE_ANSWER;
import static com.qianlei.zhifou.common.Constant.UserEventConstant.DynamicAction.CREATE_ANSWER;
import static com.qianlei.zhifou.common.Constant.UserEventConstant.TABLE_NAME_ANSWER;

/** @author qianlei */
@Service
@Slf4j
@Transactional(rollbackFor = RuntimeException.class)
public class AnswerServiceImpl implements IAnswerService {
  @Resource private AnswerDao answerDao;
  @Resource private QuestionDao questionDao;
  @Resource private AnswerElasticsearchDao answerElasticsearchDao;
  @Resource private IQuestionService questionService;
  @Resource private IUserService userService;
  @Resource private AgreeDao agreeDao;
  @Resource private ObjectMapper objectMapper;
  @Resource private KafkaTemplate<String, String> kafkaTemplate;

  private static final List<String> SUPPORTED_SORT_BY_PROPERTIES =
      List.of("createTime", "updateTime");
  private static final List<String> SUPPORT_SORT_DIRECTION = List.of("asc", "desc");

  @SneakyThrows
  @Override
  public Answer createAnswer(CreateAnswerParam param, UserVo user) {
    // XSS 过滤
    param.setContent(HtmlUtils.cleanHtmlRelaxed(param.getContent()));
    if (StringUtils.isBlank(HtmlUtil.cleanHtmlTag(param.getContent()))) {
      throw new ZhiFouException("回答不能为空");
    }
    if (!questionDao.existsById(param.getQuestionId())) {
      throw new ZhiFouException("问题不存在");
    }
    if (answerDao.findByUserIdAndQuestionId(user.getId(), param.getQuestionId()) != null) {
      throw new ZhiFouException("已回答该问题");
    }
    var answer = param.toAnswer(user.getId());
    answerDao.save(answer);
    answerElasticsearchDao.save(
        new AnswerEs(answer.getId(), HtmlUtils.cleanHtmlPlain(answer.getContent())));
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
    questionService.improveQuestionHeatLevel(answer.getQuestionId(), 100L);
    return answer;
  }

  @Override
  public AnswerVo getAnswerByQuestionId(Integer answerId, @Nullable UserVo user) {
    var answer = answerDao.findById(answerId).orElseThrow(() -> new ZhiFouException("问题不存在"));
    // 回答者用户信息
    return getAnswerFromAnswer(user, answer);
  }

  /**
   * 将 Answer 类转化为 answerVo 类
   *
   * @param user 当前用户
   * @param answer 回答信息
   * @return AnswerVo
   */
  @NotNull
  private AnswerVo getAnswerFromAnswer(UserVo user, Answer answer) {
    var answerUser = userService.getUserInfoByUserId(answer.getUserId());
    var question = questionDao.findById(answer.getQuestionId()).orElseThrow();
    long agreeNumber = agreeDao.countByAnswerId(answer.getId());
    boolean canAgree =
        user == null || !agreeDao.existsByAnswerIdAndUserId(answer.getId(), user.getId());
    questionService.improveQuestionHeatLevel(answer.getQuestionId(), 1L);
    return new AnswerVo(answer, answerUser, question, canAgree, agreeNumber);
  }

  @SneakyThrows
  @Override
  public void agree(Integer answerId, UserVo user) {
    if (!answerElasticsearchDao.existsById(answerId)) {
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
  public void deleteAgree(Integer answerId, UserVo user) {
    agreeDao.deleteByAnswerIdAndUserId(answerId, user.getId());
    // 向消息队列中发送相关的消息
    var userEvent =
        new UserEvent(null, user.getId(), AGREE_ANSWER.getId(), TABLE_NAME_ANSWER, answerId, null);
    kafkaTemplate.send(DELETE_USER_EVENT_TOPIC, objectMapper.writeValueAsString(userEvent));
  }

  @Override
  public Page<AnswerVo> getAllAnswerByQuestionId(
      Integer questionId,
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
              var question = questionDao.findById(answer.getQuestionId()).orElseThrow();
              var canAgree =
                  user == null || !agreeDao.existsByAnswerIdAndUserId(answer.getId(), user.getId());
              return new AnswerVo(answer, answerUser, question, canAgree, agreeNumber);
            });
  }

  @Override
  public List<AnswerVo> getRecommendAnswer(int num, @Nullable UserVo user) {
    if (num < 1) {
      num = 1;
    }
    var totalAnswer = answerDao.count();
    if (totalAnswer == 0) {
      return new ArrayList<>();
    }
    return new Random()
        .longs(num, 0, totalAnswer)
        .mapToObj(answerDao::findOne)
        .map(answer -> getAnswerFromAnswer(user, answer))
        .collect(Collectors.toList());
  }

  @Override
  public Page<AnswerVo> searchAnswer(String keyword, int pageNum, int pageSize, UserVo user) {
    return answerElasticsearchDao
        .findAllByContentContains(keyword, PageRequest.of(pageNum, pageSize))
        .map(AnswerEs::getId)
        .map(id -> answerDao.findById(id).orElse(null))
        .map(answer -> getAnswerFromAnswer(user, answer));
  }
}
