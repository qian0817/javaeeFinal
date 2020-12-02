package com.qianlei.zhifou.service;

import com.qianlei.zhifou.pojo.Answer;
import com.qianlei.zhifou.vo.AnswerVo;
import com.qianlei.zhifou.vo.UserVo;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;

import java.util.List;

/** @author qianlei */
public interface IAnswerService {
  /**
   * 根据 id 获取回答信息
   *
   * @param answerId 回答id
   * @param user 希望获取回答的用户
   * @return 回答信息
   */
  AnswerVo getAnswerByQuestionId(Integer answerId, @Nullable UserVo user);

  /**
   * 创建回答
   *
   * @param answerEs 回答内容
   * @param user 创建者
   * @return 创建的回答信息
   */
  Answer createAnswer(Answer answerEs, UserVo user);

  /**
   * 赞同回答
   *
   * @param answerId 回答 id
   * @param user 赞同者
   */
  void agree(Integer answerId, UserVo user);

  /**
   * 取消赞同回答
   *
   * @param answerId 回答 id
   * @param user 取消赞同者
   */
  void deleteAgree(Integer answerId, UserVo user);

  /**
   * 获取每页的回答信息
   *
   * @param questionId 问题id
   * @param sortDirection 排序方向
   * @param sortBy 排序根据
   * @param pageNum 页数
   * @param pageSize 每页数量
   * @param user 用户
   * @return 回答信息
   */
  Page<AnswerVo> getAllAnswerByQuestionId(
      Integer questionId,
      String sortDirection,
      String sortBy,
      int pageNum,
      int pageSize,
      UserVo user);

  /**
   * 获取推荐的答案
   * @param num 数量
   * @return 推荐的答案
   */
  List<AnswerVo> getRecommendAnswer(int num,@Nullable UserVo user);
}
