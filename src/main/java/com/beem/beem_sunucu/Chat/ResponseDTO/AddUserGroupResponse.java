package com.beem.beem_sunucu.Chat.ResponseDTO;

import java.util.List;

public class AddUserGroupResponse {

    private Long chatId;
    private String chatTitle;
    private Long adderId;
    private List<Long> addedUserIds;
    private int totalParticipants;
    private String message;

    public AddUserGroupResponse() {}

    public AddUserGroupResponse(Long chatId, String chatTitle, Long adderId,
                                List<Long> addedUserIds, int totalParticipants, String message) {
        this.chatId = chatId;
        this.chatTitle = chatTitle;
        this.adderId = adderId;
        this.addedUserIds = addedUserIds;
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

    public Long getAdderId() {
        return adderId;
    }

    public void setAdderId(Long adderId) {
        this.adderId = adderId;
    }

    public List<Long> getAddedUserIds() {
        return addedUserIds;
    }

    public void setAddedUserIds(List<Long> addedUserIds) {
        this.addedUserIds = addedUserIds;
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
