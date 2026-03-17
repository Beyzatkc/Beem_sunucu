package com.beem.beem_sunucu.Follow;

import com.beem.beem_sunucu.Block.BlockService;
import com.beem.beem_sunucu.Block.UserBlockedEvent;
import com.beem.beem_sunucu.Follow.FollowRequest.FollowRequestRepositorty;
import com.beem.beem_sunucu.Follow.Repository.FollowRepository;
import com.beem.beem_sunucu.Follow.Repository.FollowUserView;
import com.beem.beem_sunucu.Users.User;
import com.beem.beem_sunucu.Users.User_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FollowServices {

    private final FollowRepository followRepository;
    private final User_service userService;
    private final BlockService blockService;
    private final FollowMapper mapper;

    @Autowired
    public FollowServices(
            FollowRepository followRepository,
            User_service userService,
            BlockService blockService,
            FollowMapper mapper
    ){
        this.followRepository = followRepository;
        this.userService = userService;
        this.blockService = blockService;
        this.mapper = mapper;
    }

    @Transactional
    public FollowResponse createFollow(FollowDTO followDTO){
        boolean isBlockerFollower = blockService.isBlocked(
                followDTO.getFollowerId(),
                followDTO.getFollowingId()
        );
        boolean isBlockerFollowing = blockService.isBlocked(
                followDTO.getFollowingId(),
                followDTO.getFollowerId()
        );

        if(isBlockerFollower || isBlockerFollowing)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Blocked");



        userService.existByUser(
                followDTO.getFollowerId()
        );

        User targetUser = userService.getUserById(
                followDTO.getFollowingId()
        );

        alreadyExistsFollow(followDTO);

        if(followDTO.getFollowerId().equals(followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot follow yourself");
        }

        FollowStatus status = FollowStatus.ACCEPTED;
        boolean isFollower = isFollower(followDTO);
        if(targetUser.isPrivateProfile()){
            status = FollowStatus.PENDING;
        }

        boolean isFollowing = status == FollowStatus.ACCEPTED;
        boolean isPending = status == FollowStatus.PENDING;

        Follow follow = new Follow();
        follow.setFollowerId(followDTO.getFollowerId());
        follow.setFollowingId(followDTO.getFollowingId());
        follow.setStatus(status);
        followRepository.save(follow);

        return mapper.toFollowResponse(
                targetUser,
                isFollower,
                isFollowing,
                isPending
        );
    }

    @Transactional
    public FollowResponse unFollow(FollowDTO followDTO){


        userService.existByUser(
                followDTO.getFollowerId()
        );
        User targetUser = userService.getUserById(
                followDTO.getFollowingId()
        );

        if(followDTO.getFollowerId().equals(followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot unfollow yourself");
        }

        if(!followRepository.existsByFollowerIdAndFollowingId(followDTO.getFollowerId(), followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"You are not following this user");
        }

        Follow follow = followRepository.findByFollowerIdAndFollowingId(
                followDTO.getFollowerId(),
                followDTO.getFollowingId()
        ).orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST,"You are not following this user"));

        followRepository.delete(follow);

        boolean isFollower = isFollower(followDTO);

        boolean isFollowing = false;
        boolean isPending = false;

        return mapper.toFollowResponse(
                targetUser,
                isFollower,
                isFollowing,
                isPending
        );
    }



    @Transactional
    public FollowResponse removeFollower(FollowDTO followDTO){
        User targetUser = userService.getUserById(
                followDTO.getFollowerId()
        );
        userService.existByUser(
                followDTO.getFollowingId()
        );

        if(followDTO.getFollowerId().equals(followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot unfollow yourself");
        }

        if(!followRepository.existsByFollowerIdAndFollowingId(followDTO.getFollowerId(), followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"You are not following this user");
        }

        Follow follow = followRepository.findByFollowerIdAndFollowingId(
                followDTO.getFollowerId(),
                followDTO.getFollowingId()
        ).orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST,"You are not following this user"));

        followRepository.delete(follow);

        boolean isFollower = isFollower(followDTO);

        boolean isFollowing = false;
        boolean isPending = false;

        return mapper.toFollowResponse(
                targetUser,
                isFollower,
                isFollowing,
                isPending
        );
    }

    @Transactional
    public List<FollowResponse> otherUserFollowing(Long myid, Long targetid, int page, int size){
        userService.existByUser(
                myid
        );
        userService.existByUser(
                targetid
        );

        Pageable pageable = PageRequest.of(page, size);

        Page<FollowUserView> followPage = followRepository.findFollowingWithPriority(
                targetid,
                myid,
                pageable
        );

        return lastModified(followPage);
    }

    @Transactional
    public List<FollowResponse> otherUserFollowers(Long myid, Long targetid, int page, int size){
        userService.existByUser(
                myid
        );
        userService.existByUser(
                targetid
        );

        Pageable pageable = PageRequest.of(page, size);

        Page<FollowUserView> followPage = followRepository.findFollowersWithPriority(
                targetid,
                myid,
                pageable
        );

       return lastModified(followPage);
    }

    @EventListener
    public void handleUserBlocked(UserBlockedEvent event){
        deleteFollow(event.blockerId(), event.blockedId());
        deleteFollow(event.blockedId(), event.blockerId());
    }


    private void alreadyExistsFollow(FollowDTO followDTO){
        if(followRepository.existsByFollowerIdAndFollowingId(followDTO.getFollowerId(), followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Following this user");
        }
    }

    private List<FollowResponse> lastModified(
            Page<FollowUserView> followPage
    ){
        List<Long> orderedIds = followPage.getContent()
                .stream()
                .map(FollowUserView::getUserId)
                .toList();

        List<User> users = userService.getAllByIdIn(orderedIds);


        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        it -> it
                ));


        return followPage
                .getContent()
                .stream()
                .map(view->{
                    User user = userMap.get(view.getUserId());
                    return mapper.toFollowResponse(user, view);
                })
                .toList();
    }

    private boolean isFollower(FollowDTO followDTO){
        return followRepository.existsByFollowerIdAndFollowingIdAndStatus(
                followDTO.getFollowingId(),
                followDTO.getFollowerId(),
                FollowStatus.ACCEPTED
        );
    }

    public boolean isFollowFlow(Long followerUserId, Long followingUserId, FollowStatus status){
        return followRepository.existsByFollowerIdAndFollowingIdAndStatus(
                followerUserId,
                followingUserId,
                status
        );
    }

    public Long countByFollowers(Long userId){
        return followRepository.countByFollowingIdAndStatus(userId, FollowStatus.ACCEPTED);
    }

    public Long countByPending(Long userId){
        return followRepository.countByFollowerIdAndStatus(userId, FollowStatus.PENDING);
    }

    public Long countByFollowing(Long userId){
        return followRepository.countByFollowerIdAndStatus(userId, FollowStatus.ACCEPTED);
    }

    private void deleteFollow(Long followerId, Long followingId){
        followRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
    }


}
