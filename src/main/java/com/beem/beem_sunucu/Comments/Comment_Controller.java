package com.beem.beem_sunucu.Comments;

import com.beem.beem_sunucu.Posts.Post_DTO_Request;
import com.beem.beem_sunucu.Posts.Post_DTO_Response;
import com.beem.beem_sunucu.Posts.Post_DTO_Update;
import com.beem.beem_sunucu.Users.CustomExceptions;
import com.beem.beem_sunucu.Users.User_Response_DTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class Comment_Controller {
    private final Comment_Service commentService;

    public Comment_Controller(Comment_Service commentService) {
        this.commentService = commentService;
    }
    @PostMapping("/addComment")
    public Comment_DTO_Response addComment(@Valid @RequestBody Comment_DTO_Request commentDtoRequest) {
        return commentService.commentCreate(commentDtoRequest);
    }

    @PostMapping("/addSubComment")
    public Comment_DTO_Response addSubComment(@Valid @RequestBody Comment_DTO_Request commentDtoRequest) {
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
    public ResponseEntity<String> toggleLike(
            @PathVariable Long commentId,
            @RequestParam Long userId
    ) {
        try {
            String result = commentService.toggleLike(commentId,userId);
            return ResponseEntity.ok(result);
        } catch (CustomExceptions.AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Bir hata oluştu: " + e.getMessage());
        }
    }

    @Transactional
    @GetMapping("/{commentId}/getUsersWhoLike")
    public ResponseEntity<List<User_Response_DTO>> getUsersWhoLike(
            @PathVariable Long commentId,
            @RequestParam Long currentUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
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
    public ResponseEntity<String>DeletePost(
            @PathVariable Long commentId,
            @RequestParam Long userId
    ){
        commentService.removeComment(commentId,userId);
        return ResponseEntity.ok("Post başarıyla silindi.");
    }

    @PutMapping("/{commentId}/updateComment")
    public ResponseEntity<String> UpdatePost(
            @Valid
            @PathVariable Long commentId,
            @RequestParam Long userId,
            @RequestBody Comment_DTO_Update commentDtoUpdate
    ) {
        commentService.updateComment(commentId,userId,commentDtoUpdate);
        return ResponseEntity.ok("Post başarıyla güncellendi.");
    }
}
