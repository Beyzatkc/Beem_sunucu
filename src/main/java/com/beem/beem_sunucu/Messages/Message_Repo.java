package com.beem.beem_sunucu.Messages;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface Message_Repo extends MongoRepository<Message,Long> {
    List<Message> findTop100ByChatIdOrderBySentAtDesc(Long chatId);
    List<Message>findBySentAtBefore(LocalDateTime time);
    Page<Message> findByChatIdAndSentAtBeforeOrderBySentAtDesc(Long chatId, LocalDateTime lastMessageTime, Pageable pageable);
}
