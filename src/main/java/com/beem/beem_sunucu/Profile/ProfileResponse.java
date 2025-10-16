package com.beem.beem_sunucu.Profile;

import com.beem.beem_sunucu.Posts.Post_DTO_Response;
import com.beem.beem_sunucu.Users.User_Response_DTO;

import java.util.List;

public class ProfileResponse {

    private User_Response_DTO userResponseDto;
    private int followedCount;
    private int followerCount;
    private List<Post_DTO_Response> myPost;
    private boolean isOwnProfile;
    private boolean isFollowing;
    private int postCount;

    public ProfileResponse(){}



}
