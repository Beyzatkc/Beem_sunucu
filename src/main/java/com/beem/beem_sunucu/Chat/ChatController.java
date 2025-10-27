package com.beem.beem_sunucu.Chat;

import com.beem.beem_sunucu.Chat.RequestDTO.AddUserGroupRequest;
import com.beem.beem_sunucu.Chat.RequestDTO.CreateDirectChatRequest;
import com.beem.beem_sunucu.Chat.RequestDTO.CreateGroupChatRequest;
import com.beem.beem_sunucu.Chat.RequestDTO.RemoveUserGroupRequest;
import com.beem.beem_sunucu.Chat.ResponseDTO.AddUserGroupResponse;
import com.beem.beem_sunucu.Chat.ResponseDTO.DirectChatResponse;
import com.beem.beem_sunucu.Chat.ResponseDTO.GroupChatResponse;
import com.beem.beem_sunucu.Chat.ResponseDTO.RemoveUserGroupResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chats")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/direct")
    public ResponseEntity<DirectChatResponse> createDirectChat(@RequestBody CreateDirectChatRequest dto) {
        DirectChatResponse response = chatService.createDirectChat(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/group")
    public ResponseEntity<GroupChatResponse> createGroupChat(@RequestBody CreateGroupChatRequest dto) {
        GroupChatResponse response = chatService.createGroupChat(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/addUser")
    public ResponseEntity<AddUserGroupResponse> addUserChat(@RequestBody AddUserGroupRequest dto) {
        AddUserGroupResponse response = chatService.addUserGroup(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/removeUser")
    public ResponseEntity<RemoveUserGroupResponse> removeUserChat(@RequestBody RemoveUserGroupRequest dto) {
        RemoveUserGroupResponse response = chatService.removeUserGroup(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
