package com.beem.beem_sunucu.Chat;

import com.beem.beem_sunucu.Chat.RequestDTO.*;
import com.beem.beem_sunucu.Chat.ResponseDTO.AddUserGroupResponse;
import com.beem.beem_sunucu.Chat.ResponseDTO.DirectChatResponse;
import com.beem.beem_sunucu.Chat.ResponseDTO.GroupChatResponse;
import com.beem.beem_sunucu.Chat.ResponseDTO.RemoveUserGroupResponse;
import com.beem.beem_sunucu.Users.User_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats")
public class ChatController {

    private final ChatService chatService;
    private final User_service userService;

    @Autowired
    public ChatController(ChatService chatService, User_service userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @PostMapping("/direct")
    public ResponseEntity<DirectChatResponse> createDirectChat(@RequestBody CreateDirectChatRequest dto) {
        userService.securityUser(dto.getMyId());
        DirectChatResponse response = chatService.createDirectChat(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/group")
    public ResponseEntity<GroupChatResponse> createGroupChat(@RequestBody CreateGroupChatRequest dto) {
        userService.securityUser(dto.getCreaterId());
        GroupChatResponse response = chatService.createGroupChat(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/addUser")
    public ResponseEntity<AddUserGroupResponse> addUserChat(@RequestBody AddUserGroupRequest dto) {
        userService.securityUser(dto.getAdderId());
        AddUserGroupResponse response = chatService.addUserGroup(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/removeUser")
    public ResponseEntity<RemoveUserGroupResponse> removeUserChat(@RequestBody RemoveUserGroupRequest dto) {
        userService.securityUser(dto.getRemoverId());
        RemoveUserGroupResponse response = chatService.removeUserGroup(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/role/update")
    public ResponseEntity<String> updateUserRole(@RequestBody RoleUpdateRequest request) {
        userService.securityUser(request.getMyId());
        String message = chatService.roleUpdate(request);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/exit")
    public ResponseEntity<String> exitGroup(@RequestParam Long chatId) {
        Long exitId = userService.getCurrentUserId();
        chatService.exitGroup(exitId, chatId);
        return ResponseEntity.ok("You have successfully left the group.");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Object>> getAllMyChats() {
        Long myId = userService.getCurrentUserId();
        List<Object> chats = chatService.allMyChat(myId);
        return ResponseEntity.ok(chats);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteChat(
            @RequestParam Long chatId) {
        Long myId = userService.getCurrentUserId();
        chatService.deleteChat(myId, chatId);
        return ResponseEntity.ok("Chat deleted for this user");
    }


}
