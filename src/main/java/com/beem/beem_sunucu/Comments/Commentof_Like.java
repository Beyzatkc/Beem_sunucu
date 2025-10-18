package com.beem.beem_sunucu.Comments;

import com.beem.beem_sunucu.Posts.Post;
import com.beem.beem_sunucu.Users.User;
import jakarta.persistence.*;

@Entity
@Table(name = "commentLike",uniqueConstraints = {@UniqueConstraint(columnNames = {"post_id", "user_id"})})
public class Commentof_Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_likes_id")
    private Long commentLikesId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Long getCommentLikesId() {
        return commentLikesId;
    }

    public void setCommentLikesId(Long commentLikesId) {
        this.commentLikesId = commentLikesId;
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
