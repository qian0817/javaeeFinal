package com.qianlei.zhifou.service.impl;

import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.AgreeDao;
import com.qianlei.zhifou.dao.AnswerDao;
import com.qianlei.zhifou.dao.QuestionDao;
import com.qianlei.zhifou.entity.Question;
import com.qianlei.zhifou.service.IQuestionService;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.vo.AnswerVo;
import com.qianlei.zhifou.vo.QuestionDetailVo;
import org.jetbrains.annotations.Nullable;
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
  @Autowired private AgreeDao agreeDao;
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
      Integer answerId,
      String sortBy,
      String sortDirection,
      int pageNum,
      int pageSize,
      @Nullable String token) {
    var question = questionDao.findById(answerId).orElseThrow(() -> new ZhiFouException("问题id不存在"));
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
                  var answerUser = userService.getUserInfoByUserId(answer.getUserId());
                  var agreeNumber = agreeDao.countByAnswerId(answer.getId());
                  if (token != null) {
                    var user = userService.getUserInfo(token);
                    var canAgree = !agreeDao.existsByAnswerIdAndUserId(answerId, user.getId());
                    return new AnswerVo(answer, answerUser, question, canAgree, agreeNumber);
                  } else {
                    return new AnswerVo(answer, answerUser, question, true, agreeNumber);
                  }
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
