package com.beem.beem_sunucu.Messages;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Message_DTO_Request {
    private Long chatId;
    private UserDTOSender userDTOSender;

    @NotBlank(message = "Mesaj içeriği boş olamaz")
    private String content;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chat_id) {
        this.chatId = chat_id;
    }

    public UserDTOSender getUserDTOSender() {
        return userDTOSender;
    }

    public void setUserDTOSender(UserDTOSender userDTOSender) {
        this.userDTOSender = userDTOSender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
