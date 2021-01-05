package com.qianlei.zhifou.schedule;

import com.qianlei.zhifou.service.IQuestionService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * 热榜的定时任务
 *
 * @author qianlei
 */
@Component
@Slf4j
public class HotQuestionSchedule {
  @Resource private IQuestionService questionService;
  @Resource private RedissonClient redissonClient;
  /**
   * 在每小时的59分进行执行,将前一小时的热榜复制到后一小时的热榜之中，同时将热度值减少60倍,尽量防止出现热榜为空的情况
   *
   * <p>为了速度只将前200个热榜问题进行复制
   */
  @Scheduled(cron = "30 */59 * * * ?")
  public void prepareHotQuestion() {
    // 获取下一个小时的时间
    LocalDateTime now = LocalDateTime.now().plusHours(1);
    var lock = redissonClient.getLock("HotQuestionSchedule");

    try {
      if (lock.tryLock(1, 1, TimeUnit.MINUTES)) {
        questionService
            .getHottestQuestion(200)
            .forEach(
                question ->
                    questionService.improveQuestionHeatLevel(
                        question.getQuestion().getId(),
                        question.getHot() / 60,
                        now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd:HH"))));
      }
    } catch (InterruptedException e) {
      log.error("redis 锁异常", e);
    } finally {
      lock.unlock();
    }
  }
}
