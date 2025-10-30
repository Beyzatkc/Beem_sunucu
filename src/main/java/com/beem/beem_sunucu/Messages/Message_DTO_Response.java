package com.beem.beem_sunucu.Messages;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Message_DTO_Response {
    private Long id;
    private Long chatId;
    private UserDTOSender userDTOSender;
    private String content;
    private LocalDateTime sentAt;
    private List<String> readBy = new ArrayList<>();
    private List<Long>messagesDeleteUser = new ArrayList<>();

    public Message_DTO_Response(Message message){
        this.id=message.getId();
        this.chatId= message.getChatId();
        this.userDTOSender=message.getUserDTOSender();
        this.content=message.getContent();
        this.sentAt=message.getSentAt();
        this.readBy=message.getReadBy();
        this.messagesDeleteUser=message.getMessagesDeleteUser();
    }
    public Message_DTO_Response(Message_Archive message){
        this.id=message.getId();
        this.chatId= message.getChatId();
        this.userDTOSender=message.getUserDTOSender();
        this.content=message.getContent();
        this.sentAt=message.getSentAt();
        this.readBy=message.getReadBy();
        this.messagesDeleteUser=message.getMessagesDeleteUser();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
