package com.beem.beem_sunucu.Users;

import java.time.LocalDateTime;

public class User_Response_DTO {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String surname;
    private String profile;
    private LocalDateTime Date;
    private String biography;

    public User_Response_DTO(Long id, String username, String email, String name, String surname, String profile, LocalDateTime date, String biography) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.profile = profile;
        Date = date;
        this.biography = biography;
    }
    public User_Response_DTO(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.profile = user.getProfile();
        Date = user.getDate();
        this.biography = user.getBiography();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public LocalDateTime getDate() {
        return Date;
    }

    public void setDate(LocalDateTime date) {
        Date = date;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}
