package com.beem.beem_sunucu.Profile;

import com.beem.beem_sunucu.Users.User_Response_DTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService service;

    @Autowired
    public ProfileController(ProfileService service){
        this.service = service;
    }

    @GetMapping("/myProfile/{id}")
    private ProfileResponse getMyProfile(@PathVariable Long id){
        return service.getMyProfile(id);
    }

    @GetMapping("/otherProfile/{id}/{targetId}")
    private ResponseEntity<?> getOtherUserProfile(@PathVariable Long id,
                                                @PathVariable Long targetId
    ){
        return service.getOtherUserProfile(id, targetId);
    }


    @PutMapping("/update")
    public ResponseEntity<User_Response_DTO> updateProfile(@RequestBody UpdateProfileDTO dto) {
        return ResponseEntity.ok(service.updateProfile(dto));
    }



}
