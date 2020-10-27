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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/** @author qianlei */
@Service
public class QuestionServiceImpl implements IQuestionService {
  @Autowired private QuestionDao questionDao;
  @Autowired private AnswerDao answerDao;
  @Autowired private IUserService userService;
  private static final List<String> SUPPORTED_SORT_BY_PROPERTIES =
      List.of("createTime", "updateTime");
  private static final List<String> SUPPORT_SORT_DIRECTION = List.of("asc", "desc");

  @Override
  public Question createQuestion(Question question) {
    question.setId(null);
    return questionDao.save(question);
  }

  @Override
  public QuestionDetailVo getQuestionById(
      Integer id, String sortBy, String sortDirection, int pageNum, int pageSize) {
    var question = questionDao.findById(id).orElseThrow(() -> new ZhiFouException("问题id不存在"));
    if (!SUPPORTED_SORT_BY_PROPERTIES.contains(sortBy)) {
      throw new ZhiFouException("不支持的排序类型" + sortBy);
    }
    if (!SUPPORT_SORT_DIRECTION.contains(sortDirection)) {
      throw new ZhiFouException("不支持的排序方向" + sortDirection);
    }
    var answerVos =
        answerDao
            .findAllByQuestionId(
                question.getId(),
                PageRequest.of(pageNum, pageSize, Sort.by(Sort.Order.asc(sortBy))))
            .map(
                answer -> {
                  var userId = answer.getUserId();
                  var user = userService.getUserInfoByUserId(userId);
                  return new AnswerVo(answer, user);
                });
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
