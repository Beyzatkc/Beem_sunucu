package com.beem.beem_sunucu.Follow;

import com.beem.beem_sunucu.Follow.FollowRequest.FollowResponseDTO;
import com.beem.beem_sunucu.Users.User;

public class FollowUserResponseDTO {
    private Long id;
    private String username;
    private String profile;
    private String biography;
    private FollowResponseDTO followInfo;

    public FollowUserResponseDTO(){
    }

    public FollowUserResponseDTO(User user, FollowResponseDTO followInfo){
        this.id = user.getId();
        this.username = user.getUsername();
        this.profile = user.getProfile();
        this.biography = user.getBiography();
        this.followInfo = followInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public FollowResponseDTO getFollowInfo() {
        return followInfo;
    }

    public void setFollowInfo(FollowResponseDTO followInfo) {
        this.followInfo = followInfo;
    }
}
