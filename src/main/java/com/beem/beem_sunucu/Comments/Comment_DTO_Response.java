package com.beem.beem_sunucu.Comments;

import com.beem.beem_sunucu.Posts.Post_DTO_Response;
import com.beem.beem_sunucu.Users.User_Response_DTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Comment_DTO_Response {
    private Long comment_id;
    private String parentCommentUsername;
    private Long post_id;
    private User_Response_DTO user;
    private String contents;
    private int number_of_like;
    private LocalDateTime date;
    private LocalDateTime updateDate;
    private Long parentCommentId;
    private boolean isLiked=false;
    private boolean isEdited=false;

    private List<Comment_DTO_Response> replies = new ArrayList<>();
    private boolean isRepliesVisible;

    public Comment_DTO_Response(Comment comment) {
        this.comment_id = comment.getCommentId();
        this.post_id = comment.getPost().getPostId();
        this.user = new User_Response_DTO(comment.getUser());
        this.contents = comment.getContents();
        this.number_of_like = comment.getNumberofLikes();
        this.date = comment.getCommentDate();
        this.parentCommentId = comment.getParentYorum() != null ? comment.getParentYorum().getCommentId() : null;
    }

    public Long getPost_id() {
        return post_id;
    }

    public void setPost_id(Long post_id) {
        this.post_id = post_id;
    }

    public User_Response_DTO getUser() {
        return user;
    }

    public void setUser(User_Response_DTO user) {
        this.user = user;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public int getNumber_of_like() {
        return number_of_like;
    }

    public void setNumber_of_like(int number_of_like) {
        this.number_of_like = number_of_like;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public Long getComment_id() {
        return comment_id;
    }

    public void setComment_id(Long comment_id) {
        this.comment_id = comment_id;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getParentCommentUsername() {
        return parentCommentUsername;
    }

    public void setParentCommentUsername(String parentCommentUsername) {
        this.parentCommentUsername = parentCommentUsername;
    }

    public List<Comment_DTO_Response> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment_DTO_Response> replies) {
        this.replies = replies;
    }

    public boolean isRepliesVisible() {
        return isRepliesVisible;
    }

    public void setRepliesVisible(boolean repliesVisible) {
        isRepliesVisible = repliesVisible;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}
