package com.beem.beem_sunucu.Users;

public class SimpleUserDTO {
    private Long userId;
    private String username;
    private String profileURL;

    public SimpleUserDTO(){
    }

    public SimpleUserDTO(User user){
        this.userId = user.getId();
        this.username = user.getUsername();
        this.profileURL = user.getProfile();
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

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }
}
