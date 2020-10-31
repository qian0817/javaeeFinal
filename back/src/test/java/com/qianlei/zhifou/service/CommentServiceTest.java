package com.qianlei.zhifou.service;

import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.dao.AnswerDao;
import com.qianlei.zhifou.entity.Answer;
import com.qianlei.zhifou.entity.Comment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Rollback
class CommentServiceTest {
  @Autowired ICommentService commentService;
  @Autowired AnswerDao answerDao;

  @Test
  void createNewComment() {
    Answer answer = new Answer();
    answer.setId(1);
    answerDao.save(answer);

    Comment comment = new Comment();
    comment.setContent("");

    assertThrows(
        ZhiFouException.class, () -> commentService.createNewComment(comment, "test"), "请输入评论内容");

    comment.setContent("  \r \n");
    assertThrows(
        ZhiFouException.class, () -> commentService.createNewComment(comment, "test"), "请输入评论内容");

    comment.setContent("test");
    comment.setAnswerId(10000);
    assertThrows(
        ZhiFouException.class, () -> commentService.createNewComment(comment, "test"), "回答不存在");
    comment.setAnswerId(1);
    assertDoesNotThrow(() -> commentService.createNewComment(comment, "test"));
  }
}
