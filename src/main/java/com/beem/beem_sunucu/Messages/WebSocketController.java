package com.beem.beem_sunucu.Messages;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final Message_Service messageService;

    public WebSocketController(Message_Service messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("chat.sendMessage") // /app/chat.sendMessage adresine gönderilen mesajları yakalar
    public void receiveMessage(Message_DTO_Request request) {
        messageService.sendMessage(request);
    }

    @MessageMapping("chat.readMessage") // /app/chat.readMessage
    public void markMessageAsRead(Message_DTO_Response request) {
        messageService.markAsRead(request.getId(), request.getUserDTOSender().getUsername());
    }

    @MessageMapping("chat.deleteMessage") // /app/chat.readMessage
    public void deleteMessageEveryone(Long messageId,Long currentUserId) {
        messageService.deleteFromEveryone(messageId,currentUserId);
    }
}
