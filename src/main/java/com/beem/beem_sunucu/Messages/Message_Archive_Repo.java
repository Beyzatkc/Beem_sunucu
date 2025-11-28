package com.beem.beem_sunucu.Messages;

import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface Message_Archive_Repo extends MongoRepository<Message_Archive,String > {
    //Page<Message_Archive> findByChatIdAndSentAtBeforeOrderBySentAtDesc(Long chatId, LocalDateTime lastMessageTime, Pageable pageable);
    @Query("{ 'chatId': ?0, 'sentAt': { $lt: ?1 }, 'messagesDeleteUser': { $nin: ?2 } }")
    Page<Message_Archive> findMessages(Long chatId, LocalDateTime lastMessageTime, List<Long> currentUserIds, Pageable pageable);

}
