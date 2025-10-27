package com.beem.beem_sunucu.Chat.RequestDTO;

import java.util.List;

public class RemoveUserGroupRequest {

    private Long removerId;

    private Long chatId;

    private List<Long> participantIds;

    public RemoveUserGroupRequest(){}

    public RemoveUserGroupRequest(Long removerId, Long chatId, List<Long> participantIds) {
        this.removerId = removerId;
        this.chatId = chatId;
        this.participantIds = participantIds;
    }

    public Long getRemoverId() {
        return removerId;
    }

    public void setRemoverId(Long removerId) {
        this.removerId = removerId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public List<Long> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<Long> participantIds) {
        this.participantIds = participantIds;
    }
}
