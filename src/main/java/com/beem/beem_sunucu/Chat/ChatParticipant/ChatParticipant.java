package com.beem.beem_sunucu.Chat.ChatParticipant;
import com.beem.beem_sunucu.Chat.Chat;
import com.beem.beem_sunucu.Users.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;



@Entity
@Table(name = "chat_participants")
@IdClass(ChatParticipantId.class)
public class ChatParticipant {

    @Id
    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private ChatRole role;

    @Column(name = "is_muted", nullable = false)
    private boolean isMuted = false;

    @ManyToOne
    @JoinColumn(name = "added_by")
    private User addedBy;

    @Column(name = "notifications_enabled")
    private Boolean notificationsEnabled = true;

    @PrePersist
    protected void onCreat(){
        this.joinedAt = LocalDateTime.now();
    }

    public ChatParticipant() {}

    public ChatParticipant(Chat chat, User user, ChatRole role) {
        this.chat = chat;
        this.user = user;
        this.role = role;
    }


    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    public ChatRole getRole() {
        return role;
    }

    public void setRole(ChatRole role) {
        this.role = role;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public User getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(User addedBy) {
        this.addedBy = addedBy;
    }

    public Boolean getNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(Boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public void setMuted(boolean muted) {
        isMuted = muted;
    }
}
