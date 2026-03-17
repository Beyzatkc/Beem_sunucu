package com.beem.beem_sunucu.Follow;

import com.beem.beem_sunucu.Users.User_service;
import org.springframework.data.domain.Page;
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
    public FollowResponse createFollow(@RequestBody FollowDTO followDTO){
        userService.securityUser(followDTO.getFollowerId());
        return services.createFollow(followDTO);
    }


    @PostMapping("/unfollow")
    public FollowResponse unFollow(@RequestBody FollowDTO followDTO){
        userService.securityUser(followDTO.getFollowerId());
        return services.unFollow(followDTO);
    }

    @PostMapping("/removeFollower")
    public FollowResponse removeFollower(@RequestBody FollowDTO followDTO){
        userService.securityUser(followDTO.getFollowingId());
        return services.removeFollower(followDTO);
    }

    @GetMapping("/otherUserFollowing/{targetid}")
    public List<FollowResponse> otherUserFollowing(
                                            @PathVariable Long targetid,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "20") int size ) {
        Long myid = userService.getCurrentUserId();
        return services.otherUserFollowing(myid,targetid,page,size);
    }
    @GetMapping("/otherUserFollowers/{targetid}")
    public List<FollowResponse> otherUserFollowers(
                                                          @PathVariable Long targetid,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "20") int size ) {
        Long myid = userService.getCurrentUserId();
        return services.otherUserFollowers(myid,targetid,page,size);
    }
}
