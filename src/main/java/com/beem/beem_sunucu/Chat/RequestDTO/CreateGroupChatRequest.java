package com.beem.beem_sunucu.Chat.RequestDTO;

import com.beem.beem_sunucu.Chat.ChatType;

import java.util.List;

public class CreateGroupChatRequest {

    private Long createrId;

    private ChatType chatType;

    private String title;

    private String description;

    private String avatarUrl;

    private Integer maxParticipants;

    private List<Long> participantIds;

    public CreateGroupChatRequest() {}

    public CreateGroupChatRequest(ChatType chatType, String title, String description, String avatarUrl,
                                  Integer maxParticipants, Long creatorId, List<Long> participantIds) {
        this.chatType = chatType;
        this.title = title;
        this.description = description;
        this.avatarUrl = avatarUrl;
        this.maxParticipants = maxParticipants;
        this.createrId = creatorId;
        this.participantIds = participantIds;
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

    public List<Long> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<Long> participantIds) {
        this.participantIds = participantIds;
    }

    public Long getCreaterId() {
        return createrId;
    }

    public void setCreaterId(Long createrId) {
        this.createrId = createrId;
    }
}
