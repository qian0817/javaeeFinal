package com.qianlei.zhifou.service.impl;

import cn.hutool.http.HtmlUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianlei.zhifou.bo.KafkaAddHotMessage;
import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.AnswerDao;
import com.qianlei.zhifou.dao.QuestionDao;
import com.qianlei.zhifou.dao.es.QuestionElasticsearchDao;
import com.qianlei.zhifou.po.Question;
import com.qianlei.zhifou.po.es.QuestionEs;
import com.qianlei.zhifou.requestparam.CreateQuestionParam;
import com.qianlei.zhifou.service.IQuestionService;
import com.qianlei.zhifou.utils.HtmlUtils;
import com.qianlei.zhifou.vo.QuestionHotVo;
import com.qianlei.zhifou.vo.QuestionVo;
import com.qianlei.zhifou.vo.UserVo;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.qianlei.zhifou.common.Constant.HotQuestionConstant.HOT_QUESTION_TIME_FORMATTER;
import static com.qianlei.zhifou.common.Constant.KafkaConstant.IMPROVE_HOT_TOPIC;
import static com.qianlei.zhifou.common.Constant.RedisConstant.HOT_QUESTION_REDIS_PREFIX;

/** @author qianlei */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class QuestionServiceImpl implements IQuestionService {
  @Resource private QuestionElasticsearchDao questionElasticsearchDao;
  @Resource private AnswerDao answerDao;
  @Resource private QuestionDao questionDao;
  @Resource private ObjectMapper objectMapper;
  @Resource private StringRedisTemplate stringRedisTemplate;
  @Resource private KafkaTemplate<String, String> kafkaTemplate;
  @Resource private RedissonClient redissonClient;

  @Override
  public Question createQuestion(CreateQuestionParam param) {
    // XSS 过滤
    param.setContent(HtmlUtils.cleanHtmlRelaxed(param.getContent()));
    if (StringUtils.isBlank(HtmlUtil.cleanHtmlTag(param.getContent()))) {
      throw new ZhiFouException("问题内容不能为空");
    }
    if (StringUtils.isBlank(param.getTitle())) {
      throw new ZhiFouException("标题不能为空");
    }
    var question = param.toQuestion();
    questionDao.save(question);
    RBloomFilter<Object> bloomFilter = getQuestionBloomFilter();
    bloomFilter.add(question.getId());
    return question;
  }

  /**
   * 获取问题对应的布隆过滤器
   *
   * @return 对应的布隆过滤器
   */
  private RBloomFilter<Object> getQuestionBloomFilter() {
    var bloomFilter = redissonClient.getBloomFilter("zhifou:question:bloom");
    // 初始化布隆过滤器，预计统计元素数量为 100000 ，期望误差率为0.03
    bloomFilter.tryInit(100000, 0.03);
    return bloomFilter;
  }

  @SneakyThrows
  @KafkaListener(topics = IMPROVE_HOT_TOPIC, groupId = "improve-hot")
  public void improveQuestionHeatLevel(ConsumerRecord<String, String> record) {
    // 设置每小时的热榜
    var addHotMessage = objectMapper.readValue(record.value(), KafkaAddHotMessage.class);
    var currentHour = LocalDateTime.now().format(HOT_QUESTION_TIME_FORMATTER);
    improveQuestionHeatLevel(
        addHotMessage.getQuestionId(), addHotMessage.getHot().longValue(), currentHour);
  }

  @Override
  public void improveQuestionHeatLevel(Integer questionId, Long number, String time) {
    if (number <= 0) {
      return;
    }
    // 检查格式是否符合要求
    LocalDateTime.parse(time, HOT_QUESTION_TIME_FORMATTER);
    stringRedisTemplate
        .boundZSetOps(HOT_QUESTION_REDIS_PREFIX + time)
        .incrementScore(questionId.toString(), number);
  }

  @Override
  public Page<QuestionVo> searchQuestion(
      String keyword, int pageNum, int pageSize, @Nullable UserVo user) {
    return questionElasticsearchDao
        .findAllByTitleContaining(keyword, PageRequest.of(pageNum, pageSize))
        .map(QuestionEs::getId)
        .map(id -> getQuestionById(id).orElse(null))
        .map(question -> assembleQuestionVo(user, question));
  }

  @SneakyThrows
  @Override
  public QuestionVo userVisitQuestion(Integer questionId, UserVo user) {
    var question =
        QuestionServiceImpl.this
            .getQuestionById(questionId)
            .orElseThrow(() -> new ZhiFouException("问题id不存在"));
    kafkaTemplate.send(
        IMPROVE_HOT_TOPIC, objectMapper.writeValueAsString(new KafkaAddHotMessage(questionId, 1)));
    return assembleQuestionVo(user, question);
  }

  @SneakyThrows
  @NotNull
  private Optional<Question> getQuestionById(Integer questionId) {
    String cacheKey = "zhifou:question:id:" + questionId;
    var cachedQuestion = stringRedisTemplate.opsForValue().get(cacheKey);
    if (cachedQuestion != null) {
      // 从缓存中获取
      return Optional.of(objectMapper.readValue(cachedQuestion, Question.class));
    } else {
      // 使用布隆过滤器
      RBloomFilter<Object> bloomFilter = getQuestionBloomFilter();
      // 如果布隆过滤器中判断存在不一定存在
      // 如果不存在一定不存在
      if (bloomFilter.contains(questionId)) {
        // 从数据库中获取
        var question = questionDao.findById(questionId);
        if (question.isPresent()) {
          stringRedisTemplate
              .opsForValue()
              .set(cacheKey, objectMapper.writeValueAsString(question.get()), 1, TimeUnit.HOURS);
        }
        return question;
      } else {
        return Optional.empty();
      }
    }
  }

  @NotNull
  private QuestionVo assembleQuestionVo(@Nullable UserVo user, Question question) {
    if (user == null) {
      return new QuestionVo(question, true, null);
    }
    var myAnswer = answerDao.findByUserIdAndQuestionId(user.getId(), question.getId());
    if (myAnswer == null) {
      return new QuestionVo(question, true, null);
    } else {
      return new QuestionVo(question, false, myAnswer.getId());
    }
  }

  @Override
  public List<QuestionHotVo> getHottestQuestion(int num) {
    var currentHour = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy:MM:dd:HH"));
    // 从 redis 之中获取热榜的数据
    var questionIdList =
        stringRedisTemplate
            .boundZSetOps(HOT_QUESTION_REDIS_PREFIX + currentHour)
            .reverseRangeWithScores(0, num - 1);
    if (questionIdList == null) {
      return new ArrayList<>();
    }
    return questionIdList.stream()
        .map(
            tuple -> {
              if (tuple.getScore() == null || tuple.getValue() == null) {
                return null;
              }
              var questionId = Integer.valueOf(tuple.getValue());
              var score = tuple.getScore().longValue();
              var question = getQuestionById(questionId).orElseThrow();
              return new QuestionHotVo(question, score);
            })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  @Override
  public List<QuestionVo> getRandomQuestion(int num, @Nullable UserVo user) {
    if (num < 1) {
      num = 1;
    }
    var totalQuestion = questionDao.count();
    if (totalQuestion == 0) {
      return new ArrayList<>();
    }
    return new Random()
        .longs(num, 0, totalQuestion)
        .mapToObj(questionDao::findOne)
        .map(question -> assembleQuestionVo(user, question))
        .collect(Collectors.toList());
  }
}
