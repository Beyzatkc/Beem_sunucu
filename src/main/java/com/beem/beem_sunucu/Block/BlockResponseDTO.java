package com.beem.beem_sunucu.Block;

import java.time.LocalDateTime;

public class BlockResponseDTO {
    private Long id;
    private Long blockerId;
    private String blockerUsername;
    private Long blockedId;
    private String blockedUsername;
    private LocalDateTime createdAt;

    public BlockResponseDTO(){}

    public BlockResponseDTO(Block b) {
        this.id = b.getId();
        this.blockerId = b.getBlocker().getId();
        this.blockerUsername = b.getBlocker().getUsername();
        this.blockedId = b.getBlocked().getId();
        this.blockedUsername = b.getBlocked().getUsername();
        this.createdAt = b.getCreatedAt();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBlockerId() {
        return blockerId;
    }

    public void setBlockerId(Long blockerId) {
        this.blockerId = blockerId;
    }

    public String getBlockerUsername() {
        return blockerUsername;
    }

    public void setBlockerUsername(String blockerUsername) {
        this.blockerUsername = blockerUsername;
    }

    public Long getBlockedId() {
        return blockedId;
    }

    public void setBlockedId(Long blockedId) {
        this.blockedId = blockedId;
    }

    public String getBlockedUsername() {
        return blockedUsername;
    }

    public void setBlockedUsername(String blockedUsername) {
        this.blockedUsername = blockedUsername;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
