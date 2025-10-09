package com.beem.beem_sunucu.Posts;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class Post_DTO_Response {
    private Long post_id;

    private Long user_id;

    private String postName;

    private String contents;

    private int numberof_likes;

    private LocalDateTime postDate;

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

    public int getNumberof_likes() {
        return numberof_likes;
    }

    public void setNumberof_likes(int numberof_likes) {
        this.numberof_likes = numberof_likes;
    }

    public LocalDateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDateTime postDate) {
        this.postDate = postDate;
    }

    public Post_DTO_Response(Post post) {
        this.post_id = post.getPostId();
        this.user_id = post.getUser().getId();
        this.postName = post.getPostName();
        this.contents = post.getContents();
        this.numberof_likes = post.getNumberofLikes();
        this.postDate = post.getPostDate();
    }
}
