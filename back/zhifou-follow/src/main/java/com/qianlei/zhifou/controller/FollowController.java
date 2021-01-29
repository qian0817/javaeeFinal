package com.qianlei.zhifou.controller;

import com.qianlei.zhifou.service.IDynamicService;
import com.qianlei.zhifou.service.IFollowService;
import com.qianlei.zhifou.vo.DynamicVo;
import com.qianlei.zhifou.vo.DynamicWithUserVo;
import com.qianlei.zhifou.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/** @author qianlei */
@RestController
@RequestMapping("/api/followers")
@Slf4j
public class FollowController {
  @Resource private IFollowService followService;

  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "关注某人")
  @PostMapping("/{follower}/following/")
  public void follow(
      @Parameter(description = "被关注的人的用户 id") @PathVariable("follower") Integer follower,
      @AuthenticationPrincipal UserVo following) {
    followService.follow(follower, following.getId());
  }

  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "取消关注")
  @DeleteMapping("/{follower}/following/")
  public void unfollow(
      @Parameter(description = "被取消关注的人的用户 id") @PathVariable("follower") Integer follower,
      @AuthenticationPrincipal UserVo following) {
    followService.unfollow(follower, following.getId());
  }

  @GetMapping("/user/{userId}/follower/count")
  public Long countFollower(@PathVariable("userId") Integer userId) {
    return followService.countFollower(userId);
  }

  @GetMapping("/user/{userId}/following/count")
  public Long countFollowing(@PathVariable("userId") Integer userId) {
    return followService.countFollowing(userId);
  }

  @GetMapping("{followerUserId}/following/{followingUserId}")
  public Boolean isFollowing(@PathVariable("followerUserId")Integer followerUserId,@PathVariable("followingUserId") Integer followingUserId) {
    log.info(followerUserId + " " + followingUserId);
    return followService.existByFollowerAndFollowing(followerUserId, followingUserId);
  }
}
