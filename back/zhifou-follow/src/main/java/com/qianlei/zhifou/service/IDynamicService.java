package com.qianlei.zhifou.service;

import com.qianlei.zhifou.vo.DynamicVo;
import com.qianlei.zhifou.vo.DynamicWithUserVo;
import com.qianlei.zhifou.vo.UserVo;
import org.springframework.data.domain.Page;

/** @author qianlei */
public interface IDynamicService {
  /**
   * 获取指定用户的所有动态
   *
   * @param userId 用户 id
   * @param user 需要查看的用户
   * @param pageNum 页码
   * @param pageSize 每页数量
   * @return 指定用户的所有动态
   */
  Page<DynamicVo> getDynamicByUser(Integer userId, UserVo user, Integer pageNum, Integer pageSize);

  /**
   * 获取指定用户关注的人的所有动态
   *
   * @param user 用户
   * @param pageNum 页码
   * @param pageSize 每页数量
   * @return 用户关注的人的所有动态
   */
  Page<DynamicWithUserVo> getDynamicVoUserFollowing(UserVo user, Integer pageNum, Integer pageSize);
}
