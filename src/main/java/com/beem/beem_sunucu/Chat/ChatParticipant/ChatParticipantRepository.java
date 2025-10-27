package com.beem.beem_sunucu.Chat.ChatParticipant;

import com.beem.beem_sunucu.Chat.Chat;
import com.beem.beem_sunucu.Users.User;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, ChatParticipantId> {

    List<ChatParticipant> findByChat(Chat chat);

    List<ChatParticipant> findByUser(User user);

    boolean existsByChatAndUser(Chat chat, User user);

    Optional<ChatParticipant> findByChatAndUser(Chat chat, User user);

    List<ChatParticipant> findAllByChatAndUserIn(Chat chat,List<User> users);

    List<ChatParticipant> findAllByChatAndUserIdIn(Chat chat, List<Long> userIds);

    List<ChatParticipant> findByChatOrderByRoleAsc(Chat chat);

    List<ChatParticipant> findByChatAndRole(Chat chat, ChatRole role);

    @Modifying
    @Transactional
    @Query(
            "UPDATE chat_participants cp set cp.role = :role where cp.chat_id = :chatId and cp.user_id = :userId"
    )
    int updateUserRole(@Param("chatId") Long chatId, @Param("userId") Long userId, @Param("role") ChatRole role);
}
