package com.qianlei.zhifou.service;

import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.QuestionDao;
import com.qianlei.zhifou.entity.Answer;
import com.qianlei.zhifou.entity.Question;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Rollback
class AnswerServiceTest {
  @Autowired IAnswerService answerService;
  @Autowired QuestionDao questionDao;

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

    questionDao.save(new Question(1, "test", "test", "test"));
    assertDoesNotThrow(() -> answerService.createAnswer(newAnswer, "test"));
  }

  @Test
  public void agreeTest() {
    Answer answer = new Answer();
    answer.setContent("test");
    answer.setQuestionId(1);
    answerService.createAnswer(answer, "test");

    assertThrows(
        ZhiFouException.class, () -> answerService.agree(answer.getId() + 1, "test"), "回答不存在");
    assertDoesNotThrow(() -> answerService.agree(answer.getId(), "test"));
    assertThrows(
        ZhiFouException.class, () -> answerService.agree(answer.getId(), "test"), "不能重复点赞");
  }

  @Test
  public void getAnswerByIdTest() {

    Answer answer = new Answer();
    answer.setContent("test");
    answer.setQuestionId(1);
    answerService.createAnswer(answer, "test");

    assertThrows(
        ZhiFouException.class,
        () -> answerService.getAnswerByQuestionId(answer.getId() + 1, "test"),
        "不存在的问题id");

    assertTrue(answerService.getAnswerByQuestionId(answer.getId(), null).getCanAgree());
  }

  @Test
  public void deleteAgreeTest() {
    Answer answer = new Answer();
    answer.setContent("test");
    answer.setQuestionId(1);
    answerService.createAnswer(answer, "test");

    assertDoesNotThrow(() -> answerService.deleteAgree(answer.getId(), "test"));
  }
}
