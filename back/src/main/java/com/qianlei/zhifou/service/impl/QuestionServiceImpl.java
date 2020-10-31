package com.qianlei.zhifou.service.impl;

import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.QuestionDao;
import com.qianlei.zhifou.entity.Question;
import com.qianlei.zhifou.service.IQuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** @author qianlei */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class QuestionServiceImpl implements IQuestionService {
  @Autowired private QuestionDao questionDao;

  @Override
  public Question createQuestion(Question question) {
    question.setId(null);
    if (StringUtils.isBlank(question.getContent())) {
      throw new ZhiFouException("问题内容不能为空");
    }
    if (StringUtils.isBlank(question.getTitle())) {
      throw new ZhiFouException("标题不能为空");
    }
    question.setId(null);
    return questionDao.save(question);
  }

  @Override
  public Question getQuestionById(Integer answerId) {
    return questionDao.findById(answerId).orElseThrow(() -> new ZhiFouException("问题id不存在"));
  }

  @Override
  public List<Question> getRandomQuestion(int num) {
    if (num < 1) {
      num = 1;
    }
    return questionDao.findRandomQuestion(num);
  }
}
