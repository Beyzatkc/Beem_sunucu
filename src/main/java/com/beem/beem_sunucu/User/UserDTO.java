package com.beem.beem_sunucu.User;

public class UserDTO{

    private Long id;
    private String username;
    private String biography;

    public UserDTO(){

    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.biography = user.getBiography();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
