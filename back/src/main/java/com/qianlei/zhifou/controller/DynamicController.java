package com.qianlei.zhifou.controller;

import com.qianlei.zhifou.service.IDynamicService;
import com.qianlei.zhifou.vo.DynamicVo;
import com.qianlei.zhifou.vo.DynamicWithUserVo;
import com.qianlei.zhifou.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/** @author qianlei */
@RestController
@RequestMapping("/api/dynamic")
public class DynamicController {
  @Autowired private IDynamicService dynamicService;

  @GetMapping("/")
  public Page<DynamicWithUserVo> getDynamic(
      @RequestAttribute("user") UserVo user,
      @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
    return dynamicService.getDynamicVoUserFollowing(user.getId(), pageNum, pageSize);
  }

  @GetMapping("/user/{userId}")
  public Page<DynamicVo> getUserDynamic(
      @PathVariable("userId") Integer userId,
      @RequestParam(value = "pageNum", defaultValue = "0") Integer pageNum,
      @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
    return dynamicService.getDynamicByUser(userId, pageNum, pageSize);
  }
}
