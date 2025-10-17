package com.beem.beem_sunucu.Profile;

public class UpdateProfileDTO {

    private Long userid;
    private String profile;
    private String biography;

    public UpdateProfileDTO(){}

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

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }
}
