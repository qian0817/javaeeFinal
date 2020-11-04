package com.qianlei.zhifou.service;

import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.entity.Answer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Rollback
class AnswerServiceTest {
  @Autowired IAnswerService answerService;

  @Test
  public void createAnswerTest() {
    Answer newAnswer = new Answer();
    assertThrows(
        ZhiFouException.class, () -> answerService.createAnswer(newAnswer, "test"), "回答不能为空");

    newAnswer.setContent("  \t \n");
    assertThrows(
        ZhiFouException.class, () -> answerService.createAnswer(newAnswer, "test"), "回答不能为空");

    newAnswer.setContent("test");
    newAnswer.setQuestionId(1);

    assertThrows(
        ZhiFouException.class, () -> answerService.createAnswer(newAnswer, "test"), "问题不存在");
  }

  @Test
  public void agreeTest() {
    assertThrows(ZhiFouException.class, () -> answerService.agree(1, "test"), "回答不存在");
  }

  @Test
  public void getAnswerByIdTest() {
    assertThrows(
        ZhiFouException.class, () -> answerService.getAnswerByQuestionId(1, "test"), "问题不存在");
  }
}
