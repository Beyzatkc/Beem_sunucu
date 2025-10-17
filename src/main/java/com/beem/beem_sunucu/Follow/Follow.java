package com.beem.beem_sunucu.Follow;

import com.beem.beem_sunucu.Follow.FollowRequest.FollowSendRequest;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "follows")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long followingId;

    @Column(nullable = false)
    private Long followedId;

    @Column(nullable = false)
    private LocalDateTime date;


    @PrePersist
    protected void onCreat(){
        this.date = LocalDateTime.now();
    }

    public Follow() {}

    public Follow(FollowSendRequest request){
        this.followingId = request.getRequesterId();
        this.followedId = request.getRequestedId();
    }

    public Long getId() {
        return id;
    }

    public Long getFollowingId() {
        return followingId;
    }

    public void setFollowingId(Long followingId) {
        this.followingId = followingId;
    }

    public Long getFollowedId() {
        return followedId;
    }

    public void setFollowedId(Long followedId) {
        this.followedId = followedId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
