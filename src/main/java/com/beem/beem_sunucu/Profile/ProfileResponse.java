package com.beem.beem_sunucu.Profile;

import com.beem.beem_sunucu.Posts.Post_DTO_Response;
import com.beem.beem_sunucu.Users.User_Response_DTO;

import java.util.List;

public class ProfileResponse {

    private User_Response_DTO userResponseDto;
    private Long followedCount;
    private Long followerCount;
    private List<Post_DTO_Response> myPost;
    private boolean isOwnProfile;
    private boolean isFollowing;
    private boolean isFollower;
    private Long postCount;

    public ProfileResponse(){}


    public ProfileResponse(User_Response_DTO userResponseDto, Long followedCount, Long followerCount, List<Post_DTO_Response> myPost, boolean isOwnProfile, boolean isFollowing, boolean isFollower, Long postCount) {
        this.userResponseDto = userResponseDto;
        this.followedCount = followedCount;
        this.followerCount = followerCount;
        this.myPost = myPost;
        this.isOwnProfile = isOwnProfile;
        this.isFollowing = isFollowing;
        this.isFollower = isFollower;
        this.postCount = postCount;
    }


    public User_Response_DTO getUserResponseDto() {
        return userResponseDto;
    }

    public void setUserResponseDto(User_Response_DTO userResponseDto) {
        this.userResponseDto = userResponseDto;
    }

    public Long getFollowedCount() {
        return followedCount;
    }

    public void setFollowedCount(Long followedCount) {
        this.followedCount = followedCount;
    }

    public Long getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(Long followerCount) {
        this.followerCount = followerCount;
    }

    public List<Post_DTO_Response> getMyPost() {
        return myPost;
    }

    public void setMyPost(List<Post_DTO_Response> myPost) {
        this.myPost = myPost;
    }

    public boolean isOwnProfile() {
        return isOwnProfile;
    }

    public void setOwnProfile(boolean ownProfile) {
        isOwnProfile = ownProfile;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }

    public boolean isFollower() {
        return isFollower;
    }

    public void setFollower(boolean follower) {
        isFollower = follower;
    }

    public Long getPostCount() {
        return postCount;
    }

    public void setPostCount(Long postCount) {
        this.postCount = postCount;
    }
}
