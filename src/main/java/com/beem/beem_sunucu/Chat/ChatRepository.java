package com.beem.beem_sunucu.Chat;

import com.beem.beem_sunucu.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findByCreatedBy(User createdBy);

    List<Chat> findByTitleContainingIgnoreCase(String title);

    List<Chat> findByChatType(ChatType chatType);

    List<Chat> findByCreatedByAndChatType(User createdBy, ChatType chatType);

    List<Chat> findByMaxParticipantsLessThanEqual(Integer max);

    Optional<Chat> findByTitle(String title);

    List<Chat> findByAvatarUrlIsNotNull();

    List<Chat> findByCreatedAtBetween(java.time.LocalDateTime start, java.time.LocalDateTime end);
}
