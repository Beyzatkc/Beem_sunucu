package com.beem.beem_sunucu.Comments;

import com.beem.beem_sunucu.Posts.Post_DTO_Response;
import com.beem.beem_sunucu.Users.User_Response_DTO;

import java.time.LocalDateTime;

public class Comment_DTO_Response {
    private Long comment_id;
    private Long post_id;
    private User_Response_DTO userResponseDto;
    private String contents;
    private int number_of_like;
    private LocalDateTime date;
    private Long parent_yorum_id;


    public Comment_DTO_Response(Comment comment) {
        this.comment_id = comment.getCommentId();
        this.post_id = comment.getPost().getPostId();
        this.userResponseDto = new User_Response_DTO(comment.getUser());
        this.contents = comment.getContents();
        this.number_of_like = comment.getNumberofLikes();
        this.date = comment.getCommentDate();
        this.parent_yorum_id = comment.getParentYorum() != null ? comment.getParentYorum().getCommentId() : null;
    }

    public Long getYorum_id() {
        return comment_id;
    }

    public void setYorum_id(Long yorum_id) {
        this.comment_id = yorum_id;
    }

    public Long getPost_id() {
        return post_id;
    }

    public void setPost_id(Long post_id) {
        this.post_id = post_id;
    }

    public User_Response_DTO getUserResponseDto() {
        return userResponseDto;
    }

    public void setUserResponseDto(User_Response_DTO userResponseDto) {
        this.userResponseDto = userResponseDto;
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

    public Long getParent_yorum_id() {
        return parent_yorum_id;
    }

    public void setParent_yorum_id(Long parent_yorum_id) {
        this.parent_yorum_id = parent_yorum_id;
    }
}
