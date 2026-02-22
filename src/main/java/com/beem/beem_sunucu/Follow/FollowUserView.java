package com.beem.beem_sunucu.Follow;

public interface FollowUserView {

    Long getUserId();

    Integer getIsFollowing();

    Integer getIsFollower();

    Integer getIsPending();
}