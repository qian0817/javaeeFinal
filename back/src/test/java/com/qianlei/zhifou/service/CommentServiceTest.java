package com.qianlei.zhifou.service;

import com.qianlei.zhifou.common.ZhiFouException;
import com.qianlei.zhifou.pojo.Comment;
import com.qianlei.zhifou.vo.UserVo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Rollback
class CommentServiceTest {
  @Autowired ICommentService commentService;

  @Test
  void createNewComment() {
    Comment comment = new Comment();
    comment.setContent("");

    assertThrows(
        ZhiFouException.class,
        () -> commentService.createNewComment(comment, new UserVo()),
        "请输入评论内容");

    comment.setContent("  \r \n");
    assertThrows(
        ZhiFouException.class,
        () -> commentService.createNewComment(comment, new UserVo()),
        "请输入评论内容");

    comment.setContent("test");
    comment.setAnswerId("10000");
    assertThrows(
        ZhiFouException.class, () -> commentService.createNewComment(comment, new UserVo()), "回答不存在");
  }
}
