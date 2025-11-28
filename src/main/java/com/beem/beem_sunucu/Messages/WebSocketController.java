package com.beem.beem_sunucu.Messages;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final Message_Service messageService;

    public WebSocketController(Message_Service messageService) {
        this.messageService = messageService;
    }

    @MessageMapping("chat.sendMessage") // /app/chat.sendMessage adresine gönderilen mesajları yakalar
    public void receiveMessage(Message_DTO_Request request) {
        System.out.println("receiveMessage tetiklendi: " + request.getContent());
        messageService.sendMessage(request);
    }

    @MessageMapping("chat.readMessage") // /app/chat.readMessage
    public void markMessageAsRead(Message_read_DTO_request request) {
        messageService.markAsRead(request.getMessageId(), request.getUsername());
    }
    @MessageMapping("chat.deleteMessage") // /app/chat.deleteMessage
        public void deleteMessageEveryone(Delete_Message_DTO_Request req) {
        messageService.deleteFromEveryone(
                req.getMessageId(),
                req.getUserId()
        );
    }
}
