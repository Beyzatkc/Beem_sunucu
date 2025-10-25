package com.beem.beem_sunucu.Chat.ChatParticipant;

import com.beem.beem_sunucu.Chat.ChatParticipant.ChatRole;

public class ParticipantDTO {
    private Long userId;
    private String username;
    private ChatRole role;
    private boolean muted;

    public ParticipantDTO() {}

    public ParticipantDTO(Long userId, String username, ChatRole role, boolean muted) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.muted = muted;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public ChatRole getRole() { return role; }
    public void setRole(ChatRole role) { this.role = role; }

    public boolean isMuted() { return muted; }
    public void setMuted(boolean muted) { this.muted = muted; }
}
