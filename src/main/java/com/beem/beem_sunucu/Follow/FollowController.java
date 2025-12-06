package com.beem.beem_sunucu.Follow;

import com.beem.beem_sunucu.Users.User_service;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/follow")
public class FollowController {

    private final FollowServices services;
    private final User_service userService;

    public FollowController(FollowServices services, User_service userService){
        this.services = services;
        this.userService = userService;
    }

    @PostMapping("/follow")
    public FollowDTO createFollow(@RequestBody FollowDTO followDTO){
        userService.securityUser(followDTO.getFollowedId());
        return services.createFollow(followDTO);
    }

    @PostMapping("/unfollow")
    public FollowDTO unFollow(@RequestBody FollowDTO followDTO){
        userService.securityUser(followDTO.getFollowedId());
        return services.unFollow(followDTO);
    }

    @GetMapping("/userFollowing")
    public List<FollowUserResponseDTO> userFollowing(
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int size){
        Long id = userService.getCurrentUserId();
        return services.userFollowing(id,page,size);
    }

    @GetMapping("/otherUserFollowing/{targetid}")
    public List<FollowUserResponseDTO> otherUserFollowing(
                                            @PathVariable Long targetid,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size ) {
        Long myid = userService.getCurrentUserId();
        return services.otherUserFollowing(myid,targetid,page,size);
    }

    @GetMapping("/userFollowed")
    public List<FollowUserResponseDTO> userFollowed(
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size){
        Long id = userService.getCurrentUserId();
        return services.userFollowed(id,page,size);
    }

    @GetMapping("/otherUserFollowed/{targetid}")
    public List<FollowUserResponseDTO> otherUserFollowed(
                                                          @PathVariable Long targetid,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "20") int size ) {
        Long myid = userService.getCurrentUserId();
        return services.otherUserFollowed(myid,targetid,page,size);
    }

}
