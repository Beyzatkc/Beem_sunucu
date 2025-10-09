package com.beem.beem_sunucu.Follow;

import java.time.LocalDateTime;

public class FollowResponseDTO {

    private Long id;
    private Long requesterId;
    private Long requestedId;
    private FollowRequestStatus status;
    private LocalDateTime date;

    public FollowResponseDTO() {}

    public FollowResponseDTO(Long id, Long requesterId, Long requestedId,
                             FollowRequestStatus status, LocalDateTime date) {
        this.id = id;
        this.requesterId = requesterId;
        this.requestedId = requestedId;
        this.status = status;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public Long getRequesterId() {
        return requesterId;
    }

    public Long getRequestedId() {
        return requestedId;
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

    public void setRequesterId(Long requesterId) {
        this.requesterId = requesterId;
    }

    public void setRequestedId(Long requestedId) {
        this.requestedId = requestedId;
    }

    public void setStatus(FollowRequestStatus status) {
        this.status = status;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
