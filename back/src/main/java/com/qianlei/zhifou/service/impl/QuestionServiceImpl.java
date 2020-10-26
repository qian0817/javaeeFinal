package com.qianlei.zhifou.service.impl;

import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.AnswerDao;
import com.qianlei.zhifou.dao.QuestionDao;
import com.qianlei.zhifou.entity.Question;
import com.qianlei.zhifou.service.IQuestionService;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.vo.AnswerVo;
import com.qianlei.zhifou.vo.QuestionDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/** @author qianlei */
@Service
public class QuestionServiceImpl implements IQuestionService {
  @Autowired private QuestionDao questionDao;
  @Autowired private AnswerDao answerDao;
  @Autowired private IUserService userService;

  @Override
  public Question createQuestion(Question question) {
    question.setId(null);
    return questionDao.save(question);
  }

  @Override
  public QuestionDetailVo getQuestionById(Integer id) {
    var question = questionDao.findById(id).orElseThrow(() -> new ZhiFouException("问题id不存在"));
    var answerVos =
        answerDao.findAllByQuestionId(question.getId()).stream()
            .map(
                answer -> {
                  var userId = answer.getUserId();
                  var user = userService.getUserInfoByUserId(userId);
                  return new AnswerVo(answer, user);
                })
            .collect(Collectors.toList());

    return new QuestionDetailVo(question, answerVos);
  }

  @Override
  public List<Question> getRandomQuestion(int num) {
    if (num < 1) {
      num = 1;
    }
    return questionDao.findRandomQuestion(num);
  }
}
