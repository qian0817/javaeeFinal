package com.qianlei.zhifou.service;

import com.qianlei.zhifou.pojo.Question;
import com.qianlei.zhifou.requestparam.CreateQuestionParam;
import com.qianlei.zhifou.vo.QuestionHotVo;
import com.qianlei.zhifou.vo.QuestionVo;
import com.qianlei.zhifou.vo.UserVo;
import org.springframework.data.domain.Page;

import java.util.List;

/** @author qianlei */
public interface IQuestionService {
  /**
   * 创建问题
   *
   * @param param 问题内容
   * @return 问题内容，添加id信息
   */
  Question createQuestion(CreateQuestionParam param);

  /**
   * 根据id获取问题信息
   *
   * @param id 问题id
   * @return 问题信息
   */
  Question getQuestionById(Integer id);

  /**
   * 获取指定数量的数量的随机问题
   *
   * @param num 数量
   * @return 随机的问题
   */
  List<Question> getRandomQuestion(int num);

  /**
   * 为问题增加指定的热度
   *
   * @param questionId 问题 id
   * @param number 热度
   */
  void improveQuestionHeatLevel(Integer questionId, Long number);

  /**
   * 为问题增加指定的热度
   *
   * @param questionId 问题 id
   * @param number 热度
   * @param time 指定增加热度的时间 格式为 yyyy:MM:dd:HH
   */
  void improveQuestionHeatLevel(Integer questionId, Long number, String time);
  /**
   * 获取热榜问题
   *
   * @param num 热榜需要的数量
   * @return 热榜问题
   */
  List<QuestionHotVo> getHottestQuestion(int num);

  Page<Question> searchQuestion(String keyword, int pageNum, int pageSize);

  QuestionVo getQuestionVoById(Integer id, UserVo user);
}
