package com.beem.beem_sunucu.Messages;

public class UserDTOSender {
    private Long userId;
    private String username;
    private String profile;

    public UserDTOSender(Long userId, String username, String profile) {
        this.userId = userId;
        this.username = username;
        this.profile = profile;
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

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
