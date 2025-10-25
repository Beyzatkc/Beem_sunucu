package com.beem.beem_sunucu.Chat.ResponseDTO;

import com.beem.beem_sunucu.Chat.ChatParticipant.ParticipantDTO;
import com.beem.beem_sunucu.Chat.ChatType;
import java.time.LocalDateTime;
import java.util.List;

public class GroupChatResponse {

    private Long groupId;
    private ChatType chatType;
    private String title;
    private String description;
    private String avatarUrl;
    private Integer maxParticipants;
    private Long creatorId;
    private List<ParticipantDTO> participants;
    private LocalDateTime createdAt;

    public GroupChatResponse() {}

    public GroupChatResponse(Long groupId, ChatType chatType, String title, String description, String avatarUrl,
                             Integer maxParticipants, Long creatorId, List<ParticipantDTO> participants, LocalDateTime createdAt) {
        this.groupId = groupId;
        this.chatType = chatType;
        this.title = title;
        this.description = description;
        this.avatarUrl = avatarUrl;
        this.maxParticipants = maxParticipants;
        this.creatorId = creatorId;
        this.participants = participants;
        this.createdAt = createdAt;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public ChatType getChatType() {
        return chatType;
    }

    public void setChatType(ChatType chatType) {
        this.chatType = chatType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<ParticipantDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantDTO> participants) {
        this.participants = participants;
    }
}
