package com.beem.beem_sunucu.Follow.FollowRequest;


public class FollowRequestDTO {

    private Long requesterId;
    private Long requestedId;

    public FollowRequestDTO() {}

    public FollowRequestDTO(Long requesterId, Long requestedId) {
        this.requesterId = requesterId;
        this.requestedId = requestedId;
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
}
