package com.qianlei.zhifou.dao;

import com.qianlei.zhifou.entity.Question;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author qianlei
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class QuestionDaoTest {
    @Autowired
    private QuestionDao questionDao;

    @Test
    public void testAdd() {
        Question question = new Question();
        question.setContent("test");
        question.setTags("test");
        question.setTitle("test");
        questionDao.save(new Question());
    }
}
