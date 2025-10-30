package com.beem.beem_sunucu.Chat.ResponseDTO;

import com.beem.beem_sunucu.Chat.ChatParticipant.ParticipantDTO;
import com.beem.beem_sunucu.Chat.ChatType;
import java.time.LocalDateTime;
import java.util.List;

public class DirectChatResponse {

    private Long chatId;
    private ChatType chatType;
    private String title;
    private String description;
    private String chatProfile;
    private LocalDateTime createdAt;

    private List<ParticipantDTO> participants;

    public DirectChatResponse() {}

    public DirectChatResponse(Long chatId, ChatType chatType, String title,
                              String description, LocalDateTime createdAt,
                              List<ParticipantDTO> participants) {
        this.chatId = chatId;
        this.chatType = chatType;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.participants = participants;
    }

    public String getChatProfile() {
        return chatProfile;
    }

    public void setChatProfile(String chatProfile) {
        this.chatProfile = chatProfile;
    }

    public Long getChatId() { return chatId; }
    public void setChatId(Long chatId) { this.chatId = chatId; }

    public ChatType getChatType() { return chatType; }
    public void setChatType(ChatType chatType) { this.chatType = chatType; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<ParticipantDTO> getParticipants() { return participants; }
    public void setParticipants(List<ParticipantDTO> participants) { this.participants = participants; }
}
