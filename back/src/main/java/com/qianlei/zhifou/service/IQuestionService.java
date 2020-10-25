package com.qianlei.zhifou.service;

import com.qianlei.zhifou.entity.Question;
import com.qianlei.zhifou.vo.QuestionDetailVo;

import java.util.List;

/** @author qianlei */
public interface IQuestionService {
  /**
   * 创建问题
   *
   * @param question 问题内容
   * @return 问题内容，添加id信息
   */
  Question createQuestion(Question question);

  /**
   * 根据id获取问题信息
   *
   * @param id 问题id
   * @return 问题信息
   */
  QuestionDetailVo getQuestionById(Integer id);

  /**
   * 获取指定数量的数量的随机问题
   *
   * @param num 数量
   * @return 随机的问题
   */
  List<Question> getRandomQuestion(int num);
}
