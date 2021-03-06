package com.qianlei.zhifou.service.impl;

import cn.hutool.http.HtmlUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianlei.zhifou.bo.KafkaAddHotMessage;
import com.qianlei.zhifou.client.UserClient;
import com.qianlei.zhifou.common.Constant;
import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.AgreeDao;
import com.qianlei.zhifou.dao.AnswerDao;
import com.qianlei.zhifou.dao.QuestionDao;
import com.qianlei.zhifou.dao.es.AnswerElasticsearchDao;
import com.qianlei.zhifou.po.Agree;
import com.qianlei.zhifou.po.Answer;
import com.qianlei.zhifou.po.es.AnswerEs;
import com.qianlei.zhifou.requestparam.CreateAnswerParam;
import com.qianlei.zhifou.service.IAnswerService;
import com.qianlei.zhifou.service.IQuestionService;
import com.qianlei.zhifou.utils.HtmlUtils;
import com.qianlei.zhifou.vo.AnswerVo;
import com.qianlei.zhifou.vo.UserEvent;
import com.qianlei.zhifou.vo.UserVo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.qianlei.zhifou.common.Constant.KafkaConstant.DELETE_USER_EVENT_TOPIC;
import static com.qianlei.zhifou.common.Constant.KafkaConstant.IMPROVE_HOT_TOPIC;
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
  @Resource private UserClient userClient;
  @Resource private AgreeDao agreeDao;
  @Resource private ObjectMapper objectMapper;
  @Resource private KafkaTemplate<String, String> kafkaTemplate;
  @Resource private StringRedisTemplate stringRedisTemplate;
  @Resource private RedissonClient redissonClient;

  private static final List<String> SUPPORTED_SORT_BY_PROPERTIES =
      List.of("createTime", "updateTime");
  private static final List<String> SUPPORT_SORT_DIRECTION = List.of("asc", "desc");

  @SneakyThrows
  @Override
  public AnswerVo createAnswer(CreateAnswerParam param, UserVo user) {
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
    //    加到布隆过滤器中
    var answerBloomFilter = getAnswerBloomFilter();
    answerBloomFilter.add(answer.getId());
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
    kafkaTemplate.send(
        IMPROVE_HOT_TOPIC,
        objectMapper.writeValueAsString(new KafkaAddHotMessage(answer.getQuestionId(), 100)));
    return assemblyAnswerVo(user, answer);
  }

  @SneakyThrows
  @Override
  public AnswerVo userViewAnswer(Integer answerId, @Nullable UserVo user) {
    var answer = getAnswerById(answerId).orElseThrow(() -> new ZhiFouException("问题不存在"));
    // 回答者用户信息
    kafkaTemplate.send(
        IMPROVE_HOT_TOPIC,
        objectMapper.writeValueAsString(new KafkaAddHotMessage(answer.getQuestionId(), 1)));
    return assemblyAnswerVo(user, answer);
  }

  @SneakyThrows
  @NotNull
  private Optional<Answer> getAnswerById(Integer answerId) {
    String cacheKey = "zhifou:answer:id:" + answerId;
    String cachedAnswer = stringRedisTemplate.opsForValue().get(cacheKey);
    if (cachedAnswer == null) {
      var bloomFilter = getAnswerBloomFilter();
      if (!bloomFilter.contains(answerId)) {
        return Optional.empty();
      }
      Optional<Answer> answer = answerDao.findById(answerId);
      if (answer.isPresent()) {
        stringRedisTemplate
            .opsForValue()
            .set(cacheKey, objectMapper.writeValueAsString(answer.get()), 1, TimeUnit.HOURS);
      }
      return answer;
    } else {
      // 从缓存中获取问题
      return Optional.of(objectMapper.readValue(cachedAnswer, Answer.class));
    }
  }

  /**
   * 获取问题对应的布隆过滤器
   *
   * @return 对应的布隆过滤器
   */
  private RBloomFilter<Object> getAnswerBloomFilter() {
    var bloomFilter = redissonClient.getBloomFilter("zhifou:answer:bloom");
    // 初始化布隆过滤器，预计统计元素数量为 100000 ，期望误差率为0.03
    if (!bloomFilter.isExists()) {
      bloomFilter.tryInit(100000, 0.03);
    }
    return bloomFilter;
  }

  /**
   * 将 Answer 类转化为 answerVo 类
   *
   * @param user 当前用户
   * @param answer 回答信息
   * @return AnswerVo
   */
  @NotNull
  private AnswerVo assemblyAnswerVo(UserVo user, Answer answer) {
    var answerUser = userClient.getUserById(answer.getUserId());
    var question = questionService.userVisitQuestion(answer.getQuestionId(), user);
    long agreeNumber = agreeDao.countByAnswerId(answer.getId());
    boolean canAgree =
        user == null || !agreeDao.existsByAnswerIdAndUserId(answer.getId(), user.getId());
    return new AnswerVo(answer, answerUser, question, canAgree, agreeNumber);
  }

  @SneakyThrows
  @Override
  public void agree(Integer answerId, UserVo user) {
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
        .map(answer -> assemblyAnswerVo(user, answer));
  }

  @Override
  public List<AnswerVo> getRecommendAnswer(int num, @Nullable UserVo user) {
    if (num < 1) {
      num = 1;
    }
    var totalAnswer = answerDao.count();
    if (totalAnswer == 0) {
      return Collections.emptyList();
    }
    return new Random()
        .longs(num, 0, totalAnswer)
        .mapToObj(answerDao::findOne)
        .map(answer -> assemblyAnswerVo(user, answer))
        .collect(Collectors.toList());
  }

  @Override
  public Page<AnswerVo> searchAnswer(String keyword, int pageNum, int pageSize, UserVo user) {
    return answerElasticsearchDao
        .findAllByContentContains(keyword, PageRequest.of(pageNum, pageSize))
        .map(AnswerEs::getId)
        .map(id -> getAnswerById(id).orElse(null))
        .map(answer -> assemblyAnswerVo(user, answer));
  }

  @Override
  public Long countAnswerByUser(Integer userId) {
    return answerDao.countByUserId(userId);
  }

  @Override
  public Long countAgreeByUserId(Integer userId) {
    return agreeDao.countByUserId(userId);
  }
}
