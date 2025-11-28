package com.beem.beem_sunucu.Messages;

public class Delete_Message_DTO_Request {
    private String  messageId;
    private Long userId;

    public String  getMessageId() {
        return messageId;
    }

    public void setMessageId(String  messageId) {
        this.messageId = messageId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

