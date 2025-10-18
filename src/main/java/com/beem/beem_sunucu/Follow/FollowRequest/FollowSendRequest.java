package com.beem.beem_sunucu.Follow.FollowRequest;


import com.beem.beem_sunucu.Users.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "followrequests")
public class FollowSendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long requesterId;

    @Column(nullable = false)
    private Long requestedId;

    @Column(nullable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FollowRequestStatus status;

    @PrePersist
    protected void onCreat(){
        this.date = LocalDateTime.now();
        this.status = FollowRequestStatus.PENDING;
    }

    public FollowSendRequest(){}

    public FollowSendRequest(FollowRequestDTO dto){
        this.requestedId = dto.getRequestedId();
        this.requesterId = dto.getRequesterId();
    }

    public Long getId() {
        return id;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public Long getRequestedId() {
        return requestedId;
    }

    public void setRequestedId(Long requestedId) {
        this.requestedId = requestedId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public FollowRequestStatus getStatus() {
        return status;
    }

    public void setStatus(FollowRequestStatus status) {
        this.status = status;
    }
}
