package com.beem.beem_sunucu.Posts;

import com.beem.beem_sunucu.Users.User;
import jakarta.persistence.*;

@Entity
@Table(name="postLikes",uniqueConstraints = {@UniqueConstraint(columnNames = {"post_id", "user_id"})})
public class Post_Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_likes_id")
    private Long postLikesId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Long getPostLikesId() {
        return postLikesId;
    }

    public void setPostLikesId(Long postLikesId) {
        this.postLikesId = postLikesId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
