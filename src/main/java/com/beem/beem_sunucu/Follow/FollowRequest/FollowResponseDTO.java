package com.beem.beem_sunucu.Follow.FollowRequest;

import com.beem.beem_sunucu.Users.SimpleUserDTO;

import java.time.LocalDateTime;

public class FollowResponseDTO {

    private Long id;
    private SimpleUserDTO requester;
    private SimpleUserDTO requested;
    private FollowRequestStatus status;
    private LocalDateTime date;
    private boolean isMyFollower;
    private boolean isFollowingYou;

    public FollowResponseDTO() {}

    public FollowResponseDTO(Long id, SimpleUserDTO requester, SimpleUserDTO requested,
                             FollowRequestStatus status, LocalDateTime date, boolean isMyFollower, boolean isFollowingYou) {
        this.id = id;
        this.requester = requester;
        this.requested = requested;
        this.status = status;
        this.date = date;
        this.isMyFollower = isMyFollower;
        this.isFollowingYou = isFollowingYou;
    }
    public FollowResponseDTO(FollowSendRequest request,SimpleUserDTO requester, SimpleUserDTO requested ){
        this.id = request.getId();
        this.requester = requester;
        this.requested = requested;
        this.status = request.getStatus();
        this.date = request.getDate();
    }

    public Long getId() {
        return id;
    }


    public FollowRequestStatus getStatus() {
        return status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setStatus(FollowRequestStatus status) {
        this.status = status;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public SimpleUserDTO getRequester() {
        return requester;
    }

    public void setRequester(SimpleUserDTO requester) {
        this.requester = requester;
    }

    public SimpleUserDTO getRequested() {
        return requested;
    }

    public void setRequested(SimpleUserDTO requested) {
        this.requested = requested;
    }

    public boolean isMyFollower() {
        return isMyFollower;
    }

    public void setMyFollower(boolean myFollower) {
        isMyFollower = myFollower;
    }

    public boolean isFollowingYou() {
        return isFollowingYou;
    }

    public void setFollowingYou(boolean followingYou) {
        isFollowingYou = followingYou;
    }
}
