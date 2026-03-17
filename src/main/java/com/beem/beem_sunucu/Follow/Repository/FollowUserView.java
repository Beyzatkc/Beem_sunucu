package com.beem.beem_sunucu.Follow.Repository;

public interface FollowUserView {

    Long getUserId();

    Integer getIsFollowing();

    Integer getIsFollower();

    Integer getIsPending();
}