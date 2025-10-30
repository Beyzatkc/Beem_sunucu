package com.beem.beem_sunucu.Chat;

import com.beem.beem_sunucu.Chat.RequestDTO.*;
import com.beem.beem_sunucu.Chat.ResponseDTO.AddUserGroupResponse;
import com.beem.beem_sunucu.Chat.ResponseDTO.DirectChatResponse;
import com.beem.beem_sunucu.Chat.ResponseDTO.GroupChatResponse;
import com.beem.beem_sunucu.Chat.ResponseDTO.RemoveUserGroupResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/role/update")
    public ResponseEntity<String> updateUserRole(@RequestBody RoleUpdateRequest request) {
        String message = chatService.roleUpdate(request);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/exit")
    public ResponseEntity<String> exitGroup(@RequestParam Long exitId, @RequestParam Long chatId) {
        chatService.exitGroup(exitId, chatId);
        return ResponseEntity.ok("You have successfully left the group.");
    }

    @GetMapping("/all/{myId}")
    public ResponseEntity<List<Object>> getAllMyChats(@PathVariable Long myId) {
        List<Object> chats = chatService.allMyChat(myId);
        return ResponseEntity.ok(chats);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteChat(
            @RequestParam Long myId,
            @RequestParam Long chatId) {

        chatService.deleteChat(myId, chatId);
        return ResponseEntity.ok("Chat deleted for this user");
    }


}
