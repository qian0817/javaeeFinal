package com.qianlei.zhifou.service.impl;

import com.qianlei.zhifou.dao.CommentDao;
import com.qianlei.zhifou.entity.Comment;
import com.qianlei.zhifou.service.ICommentService;
import com.qianlei.zhifou.service.IUserService;
import com.qianlei.zhifou.vo.CommentVo;
import com.qianlei.zhifou.vo.UserVo;
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
    comment.setUserId(user.getId());
    commentDao.save(comment);
    return new CommentVo(new UserVo(user), comment);
  }
}
