package com.beem.beem_sunucu.Posts;

import com.beem.beem_sunucu.Users.User_Response_DTO;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class Post_DTO_Response {
    private Long post_id;

    private User_Response_DTO user;

    private String postName;

    private String contents;

    private int numberof_likes;

    private LocalDateTime postDate;

    private String coverImage;

    private String categories;

    public User_Response_DTO getUser() {
        return user;
    }

    public void setUser(User_Response_DTO user) {
        this.user = user;
    }

    public Long getPost_id() {
        return post_id;
    }

    public void setPost_id(Long post_id) {
        this.post_id = post_id;
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

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public Post_DTO_Response(Post post) {
        this.post_id = post.getPostId();
        this.user = new User_Response_DTO(post.getUser());
        this.postName = post.getPostName();
        this.contents = post.getContents();
        this.numberof_likes = post.getNumberofLikes();
        this.postDate = post.getPostDate();
        this.coverImage=post.getCoverImage();
        this.categories=post.getCategories();
    }
}
