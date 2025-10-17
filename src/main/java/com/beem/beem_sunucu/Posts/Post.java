package com.beem.beem_sunucu.Posts;

import com.beem.beem_sunucu.Comments.Comment;
import com.beem.beem_sunucu.Users.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(nullable = false)
    private String postName;

    @Column(nullable = false)
    private String contents;

    @Column(name = "number_of_likes")
    private int numberofLikes = 0;

    @Column(nullable = false,name = "postDate")
    private LocalDateTime postDate;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,orphanRemoval = true)
    private java.util.List<Post_Like> likes = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Comment> yorumlar = new java.util.ArrayList<>();

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public int getNumberofLikes() {
        return numberofLikes;
    }

    public void setNumberofLikes(int numberofLikes) {
        this.numberofLikes = numberofLikes;
    }

    public LocalDateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDateTime postDate) {
        this.postDate = postDate;
    }
}
