package com.beem.beem_sunucu.Follow;

public class FollowDTO {

    private Long id;
    private Long followerId;
    private Long followingId;
    private boolean followed = false;

    public FollowDTO() {}

    public FollowDTO(Follow follow) {
        this.id = follow.getId();
        this.followerId = follow.getFollowerId();
        this.followingId = follow.getFollowingId();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public Long getFollowingId() {
        return followingId;
    }

    public void setFollowingId(Long followingId) {
        this.followingId = followingId;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }
}
