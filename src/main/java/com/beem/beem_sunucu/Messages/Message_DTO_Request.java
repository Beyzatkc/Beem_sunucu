package com.beem.beem_sunucu.Messages;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Message_DTO_Request {
    private Long chat_id;
    private UserDTOSender userDTOSender;

    @NotBlank(message = "Mesaj içeriği boş olamaz")
    private String content;

    public Long getChat_id() {
        return chat_id;
    }

    public void setChat_id(Long chat_id) {
        this.chat_id = chat_id;
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
