package com.qianlei.zhifou.service;

public interface IFollowService {
    /**
     * 关注某人
     *
     * @param followerUserId 被关注者用户 id
     * @param followingUserId 关注者用户 id
     */
    void follow(Integer followerUserId, Integer followingUserId);
    /**
     * 取消关注某人
     *
     * @param unfollowerUserId 被取消关注者用户 id
     * @param unfollowingUserId 取消关注者用户 id
     */
    void unfollow(Integer unfollowerUserId, Integer unfollowingUserId);

    Long countFollower(Integer userId);

    Long countFollowing(Integer userId);

    Boolean existByFollowerAndFollowing(Integer followerUserId, Integer followingUserId);
}
