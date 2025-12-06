package com.beem.beem_sunucu.Messages;

import com.beem.beem_sunucu.Users.User_service;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final Message_Service messageService;
    private final User_service userService;

    public WebSocketController(Message_Service messageService, User_service userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @MessageMapping("chat.sendMessage") // /app/chat.sendMessage adresine gönderilen mesajları yakalar
    public void receiveMessage(Message_DTO_Request request) {
        userService.securityUser(request.getUserDTOSender().getUserId());
        System.out.println("receiveMessage tetiklendi: " + request.getContent());
        messageService.sendMessage(request);
    }

    @MessageMapping("chat.readMessage") // /app/chat.readMessage
    public void markMessageAsRead(Message_read_DTO_request request) {
        userService.securityUser(request.getUsername());
        messageService.markAsRead(request.getMessageId(), request.getUsername());
    }
    @MessageMapping("chat.deleteMessage") // /app/chat.deleteMessage
        public void deleteMessageEveryone(Delete_Message_DTO_Request req) {
        userService.securityUser(req.getUserId());
        messageService.deleteFromEveryone(
                req.getMessageId(),
                req.getUserId()
        );
    }
}
