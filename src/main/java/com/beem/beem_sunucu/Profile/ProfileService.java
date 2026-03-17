package com.beem.beem_sunucu.Profile;


import com.beem.beem_sunucu.ApiResponse.ApiResponse;
import com.beem.beem_sunucu.Block.*;
import com.beem.beem_sunucu.Follow.FollowServices;
import com.beem.beem_sunucu.Follow.FollowStatus;
import com.beem.beem_sunucu.Follow.FollowRequest.FollowRequestService;
import com.beem.beem_sunucu.Posts.Post_DTO_Response;
import com.beem.beem_sunucu.Posts.Post_Repo;
import com.beem.beem_sunucu.Users.User;
import com.beem.beem_sunucu.Users.User_Repo;
import com.beem.beem_sunucu.Users.User_Response_DTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ProfileService {

    private final FollowServices followServices;
    private final FollowRequestService requestService;
    private final User_Repo userRepo;
    private final Post_Repo postRepo;
    private final BlockRepository blockRepository;

    @Autowired
    public ProfileService(
            FollowServices followServices, FollowRequestService requestService,
            User_Repo userRepo,
            Post_Repo postRepo,
            BlockRepository blockRepository
    ){
        this.followServices = followServices;
        this.requestService = requestService;
        this.userRepo = userRepo;
        this.postRepo = postRepo;
        this.blockRepository = blockRepository;
    }

    @Transactional
    public ApiResponse<ProfileResponse> getMyProfile(Long myid, int page, int size){
        User user = userRepo.findById(myid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        PageRequest pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                Sort.by(Sort.Direction.DESC, "postDate")
        );

        return ApiResponse.success(new ProfileResponse(
                new User_Response_DTO(user),
                followServices.countByFollowing(user.getId()),
                followServices.countByFollowers(user.getId()),
                postRepo.findByUser_Id(myid, pageable).map(Post_DTO_Response::new),
                true,
                false,
                false,
                postRepo.countByUser_Id(myid),
                false,
                followServices.countByPending(myid)
        ));
    }

    @Transactional
    public ApiResponse<ProfileResponse> getOtherUserProfile(Long myId, Long targetId, int page, int size) {
        User user = userRepo.findById(targetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Optional<Block> myBlock = blockRepository.findByBlockerIdAndBlockedId(myId, targetId);
        if (myBlock.isPresent()) {
            throw new BlockException("You blocked this user", BlockStatus.YOU_BLOCKER);
        }

        Optional<Block> targetBlock = blockRepository.findByBlockerIdAndBlockedId(targetId, myId);
        if (targetBlock.isPresent()) {
            throw new BlockException("Target blocked this you", BlockStatus.TARGET_BLOCKER);
        }

        boolean isFollowing = followServices.isFollowFlow(myId, targetId, FollowStatus.ACCEPTED);
        boolean isFollower = followServices.isFollowFlow(targetId, myId, FollowStatus.ACCEPTED);
        boolean isPending = followServices.isFollowFlow(myId, targetId, FollowStatus.PENDING);

        Long followingCount = followServices.countByFollowing(targetId);
        Long followerCount = followServices.countByFollowers(targetId);


        User_Response_DTO targetUserDto = new User_Response_DTO(user);

        Long postCount = postRepo.countByUser_Id(targetId);

        PageRequest pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                Sort.by(Sort.Direction.DESC, "postDate")
        );

        Page<Post_DTO_Response> posts = postRepo.findByUser_Id(targetId, pageable).map(Post_DTO_Response::new);

        return ApiResponse.success(new ProfileResponse(
                targetUserDto,
                followingCount,
                followerCount,
                posts,
                false,
                isFollowing,
                isFollower,
                postCount,
                isPending,
                0L
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

    @Transactional(readOnly = true)
    public Page<Post_DTO_Response> getUserPosts(Long userId, int page, int size) {
        PageRequest pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                Sort.by(Sort.Direction.DESC, "postDate")
        );

        return postRepo.findByUser_Id(userId, pageable)
                .map(Post_DTO_Response::new);
    }
}
