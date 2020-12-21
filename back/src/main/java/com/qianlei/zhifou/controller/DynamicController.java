package com.qianlei.zhifou.controller;

import com.qianlei.zhifou.service.IDynamicService;
import com.qianlei.zhifou.vo.DynamicVo;
import com.qianlei.zhifou.vo.DynamicWithUserVo;
import com.qianlei.zhifou.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/** @author qianlei */
@RestController
@RequestMapping("/api/dynamic")
public class DynamicController {
  @Autowired private IDynamicService dynamicService;

  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "获取当前登录用户关注的人的动态，结果以分页的形式展示")
  @GetMapping("/")
  public Page<DynamicWithUserVo> getDynamic(
      @AuthenticationPrincipal UserVo user,
      @Parameter(description = "页号，从0开始")
          @RequestParam(value = "pageNum", defaultValue = "0", required = false)
          Integer pageNum,
      @Parameter(description = "每页数量")
          @RequestParam(value = "pageSize", defaultValue = "10", required = false)
          Integer pageSize) {
    return dynamicService.getDynamicVoUserFollowing(user, pageNum, pageSize);
  }

  @Operation(summary = "获取指定用户的动态，结果以分页展示")
  @GetMapping("/user/{userId}")
  public Page<DynamicVo> getUserDynamic(
      @Parameter(description = "指定用户的 id") @PathVariable("userId") Integer userId,
      @AuthenticationPrincipal UserVo user,
      @Parameter(description = "页号，从0开始")
          @RequestParam(value = "pageNum", defaultValue = "0", required = false)
          Integer pageNum,
      @Parameter(description = "每页数量")
          @RequestParam(value = "pageSize", defaultValue = "10", required = false)
          Integer pageSize) {
    return dynamicService.getDynamicByUser(userId, user, pageNum, pageSize);
  }
}
