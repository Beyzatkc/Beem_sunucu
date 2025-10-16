package com.beem.beem_sunucu.Profile;


import com.beem.beem_sunucu.Follow.FollowRepository;
import com.beem.beem_sunucu.Posts.Post_Repo;
import com.beem.beem_sunucu.Users.User_Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    private final FollowRepository followRepository;
    private final User_Repo userRepo;
    private final Post_Repo postRepo;

    @Autowired
    public ProfileService(
            FollowRepository followRepository,
            User_Repo userRepo,
            Post_Repo postRepo
    ){
        this.followRepository = followRepository;
        this.userRepo = userRepo;
        this.postRepo = postRepo;
    }

    @Transactional
    public ProfileResponse getMyProfile(){

    }
}
