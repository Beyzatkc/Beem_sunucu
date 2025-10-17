package com.beem.beem_sunucu.Profile;


import com.beem.beem_sunucu.Block.Block;
import com.beem.beem_sunucu.Block.BlockRepository;
import com.beem.beem_sunucu.Block.BlockResponseDTO;
import com.beem.beem_sunucu.Follow.FollowRepository;
import com.beem.beem_sunucu.Posts.Post_Repo;
import com.beem.beem_sunucu.Users.User;
import com.beem.beem_sunucu.Users.User_Repo;
import com.beem.beem_sunucu.Users.User_Response_DTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Optional;

@Service
public class ProfileService {

    private final FollowRepository followRepository;
    private final User_Repo userRepo;
    private final Post_Repo postRepo;
    private final BlockRepository blockRepository;

    @Autowired
    public ProfileService(
            FollowRepository followRepository,
            User_Repo userRepo,
            Post_Repo postRepo,
            BlockRepository blockRepository
    ){
        this.followRepository = followRepository;
        this.userRepo = userRepo;
        this.postRepo = postRepo;
        this.blockRepository = blockRepository;
    }

    @Transactional
    public ProfileResponse getMyProfile(Long myid){
        User user = userRepo.findById(myid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Long followedCount = followRepository.countByFollowingId(myid);
        Long followerCount = followRepository.countByFollowedId(myid);
        Long postCount = postRepo.countByUser_Id(myid);
        User_Response_DTO iam = new User_Response_DTO(user);

        return new ProfileResponse(
                iam,
                followedCount,
                followerCount,
                Collections.emptyList(),
                true,
                false,
                false,
                postCount
        );
    }

    @Transactional
    public ResponseEntity<?> getOtherUserProfile(Long myId, Long targetId) {
        User user = userRepo.findById(targetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Optional<Block> myBlock = blockRepository.findByBlockerIdAndBlockedId(myId, targetId);
        if (myBlock.isPresent()) {
            return ResponseEntity.ok(new BlockResponseDTO(myBlock.get()));
        }

        Optional<Block> targetBlock = blockRepository.findByBlockerIdAndBlockedId(targetId, myId);
        if (targetBlock.isPresent()) {
            return ResponseEntity.ok(new BlockResponseDTO(targetBlock.get()));
        }

        boolean isFollowing = followRepository.existsByFollowedIdAndFollowingId(targetId, myId);
        boolean isFollower = followRepository.existsByFollowedIdAndFollowingId(myId, targetId);

        Long followedCount = followRepository.countByFollowingId(targetId);
        Long followerCount = followRepository.countByFollowedId(targetId);


        User_Response_DTO targetUserDto = new User_Response_DTO(user);

        Long postCount = postRepo.countByUser_Id(targetId);

        return ResponseEntity.ok(new ProfileResponse(
                targetUserDto,
                followedCount,
                followerCount,
                Collections.emptyList(),
                false,
                isFollowing,
                isFollower,
                postCount
        ));
    }

    @Transactional
    public User_Response_DTO updateProfile(UpdateProfileDTO dto) {
        User user = userRepo.findById(dto.getUserid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (dto.getProfile() != null) {
            user.setProfile(dto.getProfile());
        }
        if (dto.getBiography() != null) {
            user.setBiography(dto.getBiography());
        }

        userRepo.save(user);

        return new User_Response_DTO(user);
    }
}
