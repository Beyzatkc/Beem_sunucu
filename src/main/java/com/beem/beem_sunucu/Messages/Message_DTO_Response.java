package com.beem.beem_sunucu.Messages;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Message_DTO_Response {
    private Long id;
    private Long chat_id;
    private UserDTOSender userDTOSender;
    private String content;
    private LocalDateTime sentAt;
    private List<String> readBy = new ArrayList<>();

    public Message_DTO_Response(Message message){
        this.id=message.getId();
        this.chat_id= message.getChat_id();
        this.userDTOSender=message.getUserDTOSender();
        this.content=message.getContent();
        this.sentAt=message.getSentAt();
        this.readBy=message.getReadBy();
    }
    public Message_DTO_Response(Message_Archive message){
        this.id=message.getId();
        this.chat_id= message.getChat_id();
        this.userDTOSender=message.getUserDTOSender();
        this.content=message.getContent();
        this.sentAt=message.getSentAt();
        this.readBy=message.getReadBy();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public List<String> getReadBy() {
        return readBy;
    }

    public void setReadBy(List<String> readBy) {
        this.readBy = readBy;
    }
}
