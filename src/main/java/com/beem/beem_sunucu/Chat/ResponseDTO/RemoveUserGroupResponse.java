package com.beem.beem_sunucu.Chat.ResponseDTO;

import java.util.List;

public class RemoveUserGroupResponse {
    private Long chatId;
    private String chatTitle;
    private Long removerId;
    private List<Long> removedUserIds;
    private int totalParticipants;
    private String message;

    public RemoveUserGroupResponse() {
    }

    public RemoveUserGroupResponse(Long chatId, String chatTitle, Long removerId, List<Long> removedUserIds, int totalParticipants, String message) {
        this.chatId = chatId;
        this.chatTitle = chatTitle;
        this.removerId = removerId;
        this.removedUserIds = removedUserIds;
        this.totalParticipants = totalParticipants;
        this.message = message;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getChatTitle() {
        return chatTitle;
    }

    public void setChatTitle(String chatTitle) {
        this.chatTitle = chatTitle;
    }

    public Long getRemoverId() {
        return removerId;
    }

    public void setRemoverId(Long removerId) {
        this.removerId = removerId;
    }

    public List<Long> getRemovedUserIds() {
        return removedUserIds;
    }

    public void setRemovedUserIds(List<Long> removedUserIds) {
        this.removedUserIds = removedUserIds;
    }

    public int getTotalParticipants() {
        return totalParticipants;
    }

    public void setTotalParticipants(int totalParticipants) {
        this.totalParticipants = totalParticipants;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
