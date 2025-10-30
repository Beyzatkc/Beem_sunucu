package com.beem.beem_sunucu.Chat.RequestDTO;

import com.beem.beem_sunucu.Chat.ChatParticipant.ChatRole;

public class RoleUpdateRequest {

    private Long chatId;
    private Long userId;
    private ChatRole role;
    private Long myId;

    public Long getChatId() { return chatId; }
    public void setChatId(Long chatId) { this.chatId = chatId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public ChatRole getRole() { return role; }
    public void setRole(ChatRole role) { this.role = role; }

    public Long getMyId() { return myId; }
    public void setMyId(Long myId) { this.myId = myId; }
}
