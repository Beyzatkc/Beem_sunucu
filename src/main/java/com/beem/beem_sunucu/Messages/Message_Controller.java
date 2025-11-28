package com.beem.beem_sunucu.Messages;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class Message_Controller {
    private final Message_Service messageService;

    public Message_Controller(Message_Service messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public ResponseEntity<Message_DTO_Response> sendMessage(@RequestBody Message_DTO_Request request) {
        System.out.println("REST sendMessage tetiklendi: " + request.getContent());
        Message_DTO_Response response = messageService.sendMessage(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getMessages")
    public List<Message_DTO_Response> getMessages(
            @RequestParam Long chatId,
            @RequestParam Long currentUserId
    ){
        return messageService.getMessages(chatId,currentUserId);
    }
    @GetMapping("/getOldMessages")
    public List<Message_DTO_Response> getOldMessages(
            @PathVariable Long chatId,
            @RequestParam("lastMessageTime") LocalDateTime lastMessageTime,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam Long currentUserId

    ){
        return messageService.getOlderMessages(chatId,lastMessageTime,limit,currentUserId);
    }

}











