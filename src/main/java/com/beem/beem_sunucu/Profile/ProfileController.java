package com.beem.beem_sunucu.Profile;

import com.beem.beem_sunucu.Posts.Post_DTO_Response;
import com.beem.beem_sunucu.Users.User_Response_DTO;
import com.beem.beem_sunucu.Users.User_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService service;
    private final User_service userService;

    @Autowired
    public ProfileController(ProfileService service, User_service userService){
        this.service = service;
        this.userService = userService;
    }

    @GetMapping("/myProfile")
    public ProfileResponse getMyProfile(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long id= userService.getCurrentUserId();
        return service.getMyProfile(id, page, size);
    }

    @GetMapping("/otherProfile/{targetId}")
    private ResponseEntity<?> getOtherUserProfile(
            @PathVariable Long targetId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Long id= userService.getCurrentUserId();
        return service.getOtherUserProfile(id, targetId, page, size);
    }


    @PutMapping("/update")
    public ResponseEntity<User_Response_DTO> updateProfile(@RequestBody UpdateProfileDTO dto) {
        userService.securityUser(dto.getUserid());
        return ResponseEntity.ok(service.updateProfile(dto));
    }

    @GetMapping("/posts")
    public Page<Post_DTO_Response> getUserPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Long id= userService.getCurrentUserId();
        return service.getUserPosts(id, page, size);
    }
}
