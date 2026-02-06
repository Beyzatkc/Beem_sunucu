package com.beem.beem_sunucu.Comments;
import com.beem.beem_sunucu.Users.CustomExceptions;
import com.beem.beem_sunucu.Users.User_Response_DTO;
import com.beem.beem_sunucu.Users.User_service;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class Comment_Controller {
    private final Comment_Service commentService;
    private final User_service userService;

    public Comment_Controller(Comment_Service commentService, User_service userService) {
        this.commentService = commentService;
        this.userService = userService;
    }
    @PostMapping("/addComment")
    public Comment_DTO_Response addComment(@Valid @RequestBody Comment_DTO_Request commentDtoRequest) {
        userService.securityUser(commentDtoRequest.getUserId());
        return commentService.commentCreate(commentDtoRequest);
    }

    @PostMapping("/addSubComment")
    public Comment_DTO_Response addSubComment(@Valid @RequestBody Comment_DTO_Request commentDtoRequest) {
        userService.securityUser(commentDtoRequest.getUserId());
        return commentService.createSubComment(commentDtoRequest);
    }

    @GetMapping("/commentsGet")
    public List<Comment_DTO_Response>commentsGet(
            @RequestParam Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return commentService.commentsGet(postId,page,size);
    }

    @GetMapping("/subCommentsGet")
    public List<Comment_DTO_Response>subCommentsGet(
            @RequestParam Long parentCommentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ){
        return commentService.subCommentsGet(parentCommentId,page,size);
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<Map<String, String>> toggleLike(
            @PathVariable Long commentId
    ) {
        Map<String, String> message = new HashMap<>();
        Long userId= userService.getCurrentUserId();
        try {
            String result = commentService.toggleLike(commentId,userId);
            message.put("message", result);
            return ResponseEntity.ok(message);
        } catch (CustomExceptions.AuthenticationException e) {
            message.put("error",e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } catch (Exception e) {
            message.put("error",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(message);
        }
    }

    @Transactional
    @GetMapping("/{commentId}/getUsersWhoLike")
    public ResponseEntity<List<User_Response_DTO>> getUsersWhoLike(
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long currentUserId= userService.getCurrentUserId();
        try {
            List<User_Response_DTO> users = commentService.users_who_like(commentId,currentUserId,page,size);
            if (users.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @DeleteMapping("/{commentId}/deleteComment")
    public ResponseEntity<Map<String, String>>DeletePost(
            @PathVariable Long commentId
    ){
        Map<String, String> message = new HashMap<>();
        Long userId= userService.getCurrentUserId();
        commentService.removeComment(commentId,userId);
        message.put("message", "Post başarıyla silindi.");
        return ResponseEntity.ok(message);
    }

    @PutMapping("/{commentId}/updateComment")
    public ResponseEntity<Map<String, String>>UpdatePost(
            @PathVariable Long commentId,
            @Valid @RequestBody Comment_DTO_Update commentDtoUpdate
    ) {
        Map<String, String> message = new HashMap<>();
        Long userId= userService.getCurrentUserId();
        commentService.updateComment(commentId,userId,commentDtoUpdate);
        message.put("message", "Post başarıyla güncellendi.");
        return ResponseEntity.ok(message);
    }
}
