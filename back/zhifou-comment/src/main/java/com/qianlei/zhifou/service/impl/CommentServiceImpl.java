package com.qianlei.zhifou.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qianlei.zhifou.bo.KafkaAddHotMessage;
import com.qianlei.zhifou.client.AnswerClient;
import com.qianlei.zhifou.client.UserClient;
import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.CommentDao;
import com.qianlei.zhifou.requestparam.CreateCommentParam;
import com.qianlei.zhifou.service.ICommentService;
import com.qianlei.zhifou.vo.CommentVo;
import com.qianlei.zhifou.vo.UserVo;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.pqc.math.linearalgebra.IntUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.Map;

import static com.qianlei.zhifou.common.Constant.KafkaConstant.IMPROVE_HOT_TOPIC;

/** @author qianlei */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class CommentServiceImpl implements ICommentService {
  @Resource private UserClient userClient;
  @Resource private KafkaTemplate<String, String> kafkaTemplate;
  @Resource private CommentDao commentDao;
  @Resource private AnswerClient answerClient;
  @Resource private ObjectMapper objectMapper;

  @Override
  public Page<CommentVo> getComment(Integer answerId, Integer pageNum, Integer pageSize) {
    var pageable = PageRequest.of(pageNum, pageSize);
    return commentDao
        .findAllByAnswerIdOrderByCreateTimeDesc(answerId, pageable)
        .map(comment -> new CommentVo(userClient.getUserById(comment.getUserId()), comment));
  }

  @SneakyThrows
  @Override
  public CommentVo createNewComment(CreateCommentParam param, Integer answerId, UserVo user) {
    if (StringUtils.isBlank(param.getContent())) {
      throw new ZhiFouException("请输入评论内容");
    }
    var answer = answerClient.getAnswerById(answerId);
    var comment = param.toComment(answerId, user.getId());
    commentDao.save(comment);
    // 每条新的评论为问题增加 30 个热度
    var questionId = answer.getQuestionId();
    kafkaTemplate.send(
        IMPROVE_HOT_TOPIC, objectMapper.writeValueAsString(new KafkaAddHotMessage(questionId, 1)));
    return new CommentVo(user, comment);
  }
}
