package com.qianlei.zhifou.service;

import com.qianlei.zhifou.vo.DynamicVo;
import com.qianlei.zhifou.vo.DynamicWithUserVo;
import org.springframework.data.domain.Page;

/** @author qianlei */
public interface IDynamicService {
  /**
   * 获取指定用户的所有动态
   *
   * @param userId 用户 id
   * @param pageNum 页码
   * @param pageSize 每页数量
   * @return 指定用户的所有动态
   */
  Page<DynamicVo> getDynamicByUser(Integer userId, Integer pageNum, Integer pageSize);

  /**
   * 获取指定用户关注的人的所有动态
   *
   * @param userId 用户 id
   * @param pageNum 页码
   * @param pageSize 每页数量
   * @return 用户关注的人的所有动态
   */
  Page<DynamicWithUserVo> getDynamicVoUserFollowing(Integer userId, Integer pageNum, Integer pageSize);
}
