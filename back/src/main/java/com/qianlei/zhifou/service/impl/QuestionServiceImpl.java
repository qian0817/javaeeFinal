package com.qianlei.zhifou.service.impl;

import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.AnswerDao;
import com.qianlei.zhifou.dao.QuestionDao;
import com.qianlei.zhifou.entity.Question;
import com.qianlei.zhifou.service.IQuestionService;
import com.qianlei.zhifou.vo.QuestionDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/** @author qianlei */
@Service
public class QuestionServiceImpl implements IQuestionService {
  @Autowired private QuestionDao questionDao;
  @Autowired private AnswerDao answerDao;

  @Override
  public Question createQuestion(Question question) {
    question.setId(null);
    return questionDao.save(question);
  }

  @Override
  public QuestionDetailVo getQuestionById(Integer id) {
    var question = questionDao.findById(id).orElseThrow(() -> new ZhiFouException("问题id不存在"));
    var answers = answerDao.findAllByQuestionId(question.getId());
    return new QuestionDetailVo(question, answers);
  }

  @Override
  public List<Question> getRandomQuestion(int num) {
    if (num < 1) {
      num = 1;
    }
    return questionDao.findRandomQuestion(num);
  }
}
