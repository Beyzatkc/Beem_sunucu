package com.beem.beem_sunucu.Comments;

import com.beem.beem_sunucu.Posts.Post;
import com.beem.beem_sunucu.Users.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "post_id",nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(name = "contents", nullable = false)
    private String contents;

    @Column(name = "number_of_likes")
    private int numberofLikes = 0;

    @Column(nullable = false,name = "commentDate")
    private LocalDateTime commentDate;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;


    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> subComments=new ArrayList<>();

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment_Like> likes=new ArrayList<>();



    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
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

    public LocalDateTime getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(LocalDateTime commentDate) {
        this.commentDate = commentDate;
    }

    public Comment getParentYorum() {
        return parentComment;
    }

    public void setParentYorum(Comment parentYorum) {
        this.parentComment = parentYorum;
    }

    public List<Comment> getSubComments() {
        return subComments;
    }

    public void setSubComments(List<Comment> subComments) {
        this.subComments = subComments;
    }
}
