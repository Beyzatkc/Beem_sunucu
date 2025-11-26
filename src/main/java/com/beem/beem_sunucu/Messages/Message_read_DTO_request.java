package com.beem.beem_sunucu.Messages;

public class Message_read_DTO_request {
    private Long messageId;
    private String username;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
