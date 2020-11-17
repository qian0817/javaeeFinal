package com.qianlei.zhifou.service;

import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.pojo.es.Question;
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
class QuestionServiceTest {
    @Autowired private IQuestionService questionService;

    @Test
    void createQuestion() {
        Question question = new Question();
        assertThrows(ZhiFouException.class, () -> questionService.createQuestion(question), "问题内容不能为空");
        question.setContent("test");
        assertThrows(ZhiFouException.class, () -> questionService.createQuestion(question), "标题不能为空");
        question.setTitle("test");
        assertDoesNotThrow(() -> questionService.createQuestion(question));
    }

    @Test
    void getQuestionById() {
        Question question = new Question();
        question.setContent("test");
        question.setTitle("test");
        questionService.createQuestion(question);

        assertNotNull(questionService.getQuestionById(question.getId()));
    }
}
