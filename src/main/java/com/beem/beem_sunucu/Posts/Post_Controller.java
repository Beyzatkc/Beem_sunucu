package com.beem.beem_sunucu.Posts;

import com.beem.beem_sunucu.Users.CustomExceptions;
import com.beem.beem_sunucu.Users.User_Response_DTO;
import com.beem.beem_sunucu.Users.User_service;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class Post_Controller {
    private final Post_Service postService;
    private final User_service userService;

    public Post_Controller(Post_Service postService, User_service userService) {
        this.postService = postService;
        this.userService = userService;
    }
    
    @PostMapping("/addPost")
    public Post_DTO_Response addPost(@Valid @RequestBody Post_DTO_Request postDto) {
        userService.securityUser(postDto.getUser_id());
        return postService.postCreate(postDto);
    }

    @GetMapping("/getUserPosts") //
    public List<Post_DTO_Response> getUserPosts(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return postService.fetch_users_posts(userId, page, size);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<String> toggleLike(
            @PathVariable Long postId
    ) {
        Long userId= userService.getCurrentUserId();
        try {
            String result = postService.toggleLike(postId, userId);
            return ResponseEntity.ok(result);
        } catch (CustomExceptions.AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Bir hata oluştu: " + e.getMessage());
        }
    }

    @Transactional
    @GetMapping("/{postId}/getUsersWhoLike")
    public ResponseEntity<List<User_Response_DTO>> getUsersWhoLike(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long currentUserId= userService.getCurrentUserId();
        try {
            List<User_Response_DTO> users = postService.users_who_like(postId, currentUserId, page, size);
            if (users.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
    @GetMapping("/HomePagePosts")
    public ResponseEntity<List<Post_DTO_Response>> HomePagePosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Long currentUserId= userService.getCurrentUserId();
        List<Post_DTO_Response> posts = postService.homePagePosts(currentUserId, page, size);
        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(posts);
    }

    @DeleteMapping("/{postId}/deletePost")
    public ResponseEntity<String>DeletePost(
            @PathVariable Long postId
    ){
        Long userId= userService.getCurrentUserId();
        postService.deletePost(postId, userId);
        return ResponseEntity.ok("Post başarıyla silindi.");
    }

    @PutMapping("/{postId}/updatePost")
    public ResponseEntity<String> UpdatePost(
            @Valid
            @PathVariable Long postId,
            @RequestBody Post_DTO_Update postDtoUpdate
    ) {
        Long userId= userService.getCurrentUserId();
        postService.updatePost(postId, userId, postDtoUpdate);
        return ResponseEntity.ok("Post başarıyla güncellendi.");
    }
}
