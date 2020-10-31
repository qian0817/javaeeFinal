package com.qianlei.zhifou.service.impl;

import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.AnswerDao;
import com.qianlei.zhifou.dao.CommentDao;
import com.qianlei.zhifou.entity.Comment;
import com.qianlei.zhifou.service.ICommentService;
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
  @Autowired private CommentDao commentDao;
  @Autowired private IUserService userService;
  @Autowired private AnswerDao answerDao;

  @Override
  public Page<CommentVo> getComment(Integer answerId, Integer pageNum, Integer pageSize) {
    var pageable = PageRequest.of(pageNum, pageSize);
    return commentDao
        .findAllByAnswerIdOrderByCreateTime(answerId, pageable)
        .map(
            comment ->
                new CommentVo(userService.getUserInfoByUserId(comment.getUserId()), comment));
  }

  @Override
  public CommentVo createNewComment(Comment comment, String token) {
    var user = userService.getUserInfo(token);
    if (StringUtils.isBlank(comment.getContent())) {
      throw new ZhiFouException("请输入评论内容");
    }
    if (!answerDao.existsById(comment.getAnswerId())) {
      throw new ZhiFouException("回答不存在");
    }
    comment.setUserId(user.getId());
    comment.setId(null);
    comment.setCreateTime(null);
    commentDao.save(comment);
    return new CommentVo(new UserVo(user), comment);
  }
}
