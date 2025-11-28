package com.beem.beem_sunucu.Chat.RequestDTO;

import java.util.List;

public class AddUserGroupRequest {

    private Long adderId;

    private Long chatId;

    private List<Long> participantIds;

    public AddUserGroupRequest(){}

    public AddUserGroupRequest(Long adderId, List<Long> participantIds, Long  chatId) {
        this.adderId = adderId;
        this.participantIds = participantIds;
        this.chatId = chatId;
    }

    public Long getAdderId() {
        return adderId;
    }

    public void setAdderId(Long adderId) {
        this.adderId = adderId;
    }

    public List<Long> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<Long> participantIds) {
        this.participantIds = participantIds;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}
