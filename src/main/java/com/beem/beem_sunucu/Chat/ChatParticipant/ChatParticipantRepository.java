package com.beem.beem_sunucu.Chat.ChatParticipant;

import com.beem.beem_sunucu.Chat.Chat;
import com.beem.beem_sunucu.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, ChatParticipantId> {

    List<ChatParticipant> findByChat(Chat chat);

    List<ChatParticipant> findByUser(User user);

    boolean existsByChatAndUser(Chat chat, User user);

    Optional<ChatParticipant> findByChatAndUser(Chat chat, User user);

    List<ChatParticipant> findByChatOrderByRoleAsc(Chat chat);

    List<ChatParticipant> findByChatAndRole(Chat chat, ChatRole role);
}
