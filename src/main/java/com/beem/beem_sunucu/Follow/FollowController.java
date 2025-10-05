package com.beem.beem_sunucu.Follow;

import com.beem.beem_sunucu.User.UserDTO;
import com.beem.beem_sunucu.User.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/follow")
public class FollowController {

    private final FollowServices services;

    public FollowController(FollowServices services){
        this.services = services;
    }

    @PostMapping("/follow")
    public FollowDTO createFollow(@RequestBody FollowDTO followDTO){
        return services.createFollow(followDTO);
    }

    @PostMapping("/unfollow")
    public FollowDTO unFollow(@RequestBody FollowDTO followDTO){
        return services.unFollow(followDTO);
    }

    @GetMapping("/userFollowing/{id}")
    public List<UserDTO> userFollowing(Long id){

    }
}
