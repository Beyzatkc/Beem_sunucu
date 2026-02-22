package com.beem.beem_sunucu.Follow;

public class FollowResponse {
    private Long userId;
    private String username;
    private String bio;
    private String profileUrl;
    private boolean isFollower;
    private boolean isFollowing;
    private boolean isPending;

    public FollowResponse() {
    }

    public FollowResponse(Long userId, String username, String bio, String profileUrl, boolean isFollower, boolean isFollowing, boolean isPending) {
        this.userId = userId;
        this.username = username;
        this.bio = bio;
        this.profileUrl = profileUrl;
        this.isFollower = isFollower;
        this.isFollowing = isFollowing;
        this.isPending = isPending;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public boolean isFollower() {
        return isFollower;
    }

    public void setFollower(boolean follower) {
        isFollower = follower;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }
}
