package com.beem.beem_sunucu.Profile;

import com.beem.beem_sunucu.Posts.Post_DTO_Response;
import com.beem.beem_sunucu.Users.User_Response_DTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "http://localhost:3000")
public class ProfileController {

    private final ProfileService service;

    @Autowired
    public ProfileController(ProfileService service){
        this.service = service;
    }

    @GetMapping("/myProfile/{id}")
    public ProfileResponse getMyProfile(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.getMyProfile(id, page, size);
    }

    @GetMapping("/otherProfile/{id}/{targetId}")
    private ResponseEntity<?> getOtherUserProfile(
            @PathVariable Long id,
            @PathVariable Long targetId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return service.getOtherUserProfile(id, targetId, page, size);
    }


    @PutMapping("/update")
    public ResponseEntity<User_Response_DTO> updateProfile(@RequestBody UpdateProfileDTO dto) {
        return ResponseEntity.ok(service.updateProfile(dto));
    }


    @GetMapping("/{id}/posts")
    public Page<Post_DTO_Response> getUserPosts(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.getUserPosts(id, page, size);
    }



}
