package com.beem.beem_sunucu.Messages;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Document("Messages")
@CompoundIndexes({
        @CompoundIndex(name = "chat_sentAt_idx", def = "{'chatId': 1, 'sentAt': -1}")
})
public class Message {
    @Id
    private Long id;

    @Field("chat_id")
    private Long chatId;

    private UserDTOSender userDTOSender;
    private String content;
    private LocalDateTime sentAt;
    private List<String>readBy = new ArrayList<>();
    private List<Long>messagesDeleteUser = new ArrayList<>();

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

    public Long getChatId() { return chatId; }
    public void setChatId(Long chatId) { this.chatId = chatId; }

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
