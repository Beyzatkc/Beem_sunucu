package com.beem.beem_sunucu.Messages;

import com.beem.beem_sunucu.Users.User_Repo;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
@Component
public class MessageKafkaListener {
    private final SimpMessagingTemplate messagingTemplate;

    public MessageKafkaListener(SimpMessagingTemplate messagingTemplate, User_Repo userRepository) {
        this.messagingTemplate = messagingTemplate;
    }
    @KafkaListener(topics = "message-topic", groupId = "chat_group", containerFactory = "kafkaListenerContainerFactory")
    public void listenNewMessage(Message_DTO_Response message) {
        try {
            String wsDestination = "/topic/chat-" + message.getChatId();
            messagingTemplate.convertAndSend(wsDestination, message);
        } catch (Exception e) {
            System.out.println("Hatalı mesaj offset yakalandı: " + message);
        }
    }

    @KafkaListener(topics = "message-read-topic", groupId = "chat_group")
    public void listenReadMessage(Message_DTO_Response message) {
        String wsDestination = "/topic/chat-" + message.getChatId() + "-read";
        messagingTemplate.convertAndSend(wsDestination, message);
    }
    @KafkaListener(topics = "message-delete-topic", groupId = "message-group")
    public void handleDeleteEvent(Message_DTO_Response message) {
        String wsDestination = "/topic/chat-" + message.getChatId() + "-delete";
        messagingTemplate.convertAndSend(wsDestination, message);
    }



    //BİLDİRİM EKLENECEK

}


