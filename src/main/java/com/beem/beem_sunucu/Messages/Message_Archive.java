package com.beem.beem_sunucu.Messages;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document("messages_archive")
public class Message_Archive {
    @Id
    private Long id;
    private Long chat_id;
    private UserDTOSender userDTOSender;
    private String content;
    private LocalDateTime sentAt;
    private List<String> readBy;

    public Message_Archive(Message message){
        this.id = message.getId();
        this.chat_id = message.getChat_id();
        this.userDTOSender = message.getUserDTOSender();
        this.content = message.getContent();
        this.sentAt = message.getSentAt();
        this.readBy = message.getReadBy();
    }

    public UserDTOSender getUserDTOSender() {
        return userDTOSender;
    }

    public void setUserDTOSender(UserDTOSender userDTOSender) {
        this.userDTOSender = userDTOSender;
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
