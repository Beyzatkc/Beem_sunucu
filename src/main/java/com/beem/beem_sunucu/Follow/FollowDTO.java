package com.beem.beem_sunucu.Follow;

public class FollowDTO {

    private Long id;
    private Long followingId;
    private Long followedId;
    private boolean followed = false;

    public FollowDTO() {}

    public FollowDTO(Follow follow) {
        this.id = follow.getId();
        this.followingId = follow.getFollowingId();
        this.followedId = follow.getFollowedId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFollowingId() {
        return followingId;
    }

    public void setFollowingId(Long followingId) {
        this.followingId = followingId;
    }

    public Long getFollowedId() {
        return followedId;
    }

    public void setFollowedId(Long followedId) {
        this.followedId = followedId;
    }

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }
}
