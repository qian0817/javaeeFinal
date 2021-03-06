package com.qianlei.zhifou.service;

import com.qianlei.zhifou.requestparam.CreateCommentParam;
import com.qianlei.zhifou.vo.CommentVo;
import com.qianlei.zhifou.vo.UserVo;
import org.springframework.data.domain.Page;

/** @author qianlei */
public interface ICommentService {
    /**
     * 获取指定回答下的评论信息
     *
     * @param answerId 回答 id
     * @param pageNum 第几页
     * @param pageSize 每页大小
     * @return 评论信息
     */
    Page<CommentVo> getComment(Integer answerId, Integer pageNum, Integer pageSize);

    /**
     * 创建新的评论
     *
     * @param param 评论内容
     * @param answerId 评论的回答 id
     * @param user 创建者
     * @return 评论信息
     */
    CommentVo createNewComment(CreateCommentParam param, Integer answerId, UserVo user);
}
