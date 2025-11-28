package com.beem.beem_sunucu.Messages;

public class Message_read_DTO_request {
    private String  messageId;
    private String username;

    public String  getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
