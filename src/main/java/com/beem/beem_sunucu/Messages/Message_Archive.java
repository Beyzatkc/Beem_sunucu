package com.beem.beem_sunucu.Messages;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document("messages_archive")
public class Message_Archive {
    @Id
    private String id;
    private Long chatId;
    private UserDTOSender userDTOSender;
    private String content;
    private LocalDateTime sentAt;
    private List<String> readBy;
    private List<Long>messagesDeleteUser;

    public Message_Archive(Message message){
        this.id = message.getId();
        this.chatId = message.getChatId();
        this.userDTOSender = message.getUserDTOSender();
        this.content = message.getContent();
        this.sentAt = message.getSentAt();
        this.readBy = message.getReadBy();
        this.messagesDeleteUser=message.getMessagesDeleteUser();
    }

    public UserDTOSender getUserDTOSender() {
        return userDTOSender;
    }

    public void setUserDTOSender(UserDTOSender userDTOSender) {
        this.userDTOSender = userDTOSender;
    }

    public String  getId() {
        return id;
    }

    public void setId(String  id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
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

    public List<Long> getMessagesDeleteUser() {
        return messagesDeleteUser;
    }

    public void setMessagesDeleteUser(List<Long> messagesDeleteUser) {
        this.messagesDeleteUser = messagesDeleteUser;
    }
}
