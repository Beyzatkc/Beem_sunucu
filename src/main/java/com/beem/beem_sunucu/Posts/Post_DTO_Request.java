package com.beem.beem_sunucu.Posts;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class Post_DTO_Request {
    private Long post_id;

    private Long user_id;

    @NotBlank(message = "Gönderi adi boş olamaz")
    private String postName;

    @NotBlank(message = "Gönderi içeriği boş olamaz")
    private String contents;


    public Long getPost_id() {
        return post_id;
    }

    public void setPost_id(Long post_id) {
        this.post_id = post_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

}
