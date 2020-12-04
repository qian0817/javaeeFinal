package com.qianlei.zhifou.service.impl;

import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.AnswerDao;
import com.qianlei.zhifou.dao.CommentDao;
import com.qianlei.zhifou.requestparam.CreateCommentParam;
import com.qianlei.zhifou.service.ICommentService;
import com.qianlei.zhifou.service.IQuestionService;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.vo.CommentVo;
import com.qianlei.zhifou.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** @author qianlei */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class CommentServiceImpl implements ICommentService {
  @Autowired private IUserService userService;
  @Autowired private IQuestionService questionService;
  @Autowired private CommentDao commentDao;
  @Autowired private AnswerDao answerDao;

  @Override
  public Page<CommentVo> getComment(Integer answerId, Integer pageNum, Integer pageSize) {
    var pageable = PageRequest.of(pageNum, pageSize);
    return commentDao
        .findAllByAnswerIdOrderByCreateTimeDesc(answerId, pageable)
        .map(
            comment ->
                new CommentVo(userService.getUserInfoByUserId(comment.getUserId()), comment));
  }

  @Override
  public CommentVo createNewComment(CreateCommentParam param, Integer answerId, UserVo user) {
    if (StringUtils.isBlank(param.getContent())) {
      throw new ZhiFouException("请输入评论内容");
    }
    if (!answerDao.existsById(answerId)) {
      throw new ZhiFouException("回答不存在");
    }
    var comment = param.toComment(answerId);
    commentDao.save(comment);
    var answer = answerDao.findById(comment.getAnswerId()).orElseThrow();
    // 每条新的评论为问题增加 30 个热度
    questionService.improveQuestionHeatLevel(answer.getQuestionId(), 30);
    return new CommentVo(user, comment);
  }
}
