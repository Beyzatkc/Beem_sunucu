package com.beem.beem_sunucu.Follow;

import com.beem.beem_sunucu.Block.BlockService;
import com.beem.beem_sunucu.Follow.FollowRequest.FollowRequestRepositorty;
import com.beem.beem_sunucu.Follow.FollowRequest.FollowRequestStatus;
import com.beem.beem_sunucu.Follow.FollowRequest.FollowResponseDTO;
import com.beem.beem_sunucu.Follow.FollowRequest.FollowSendRequest;
import com.beem.beem_sunucu.Users.User;
import com.beem.beem_sunucu.Users.User_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FollowServices {

    private final FollowRepository followRepository;
    private final User_service userService;
    private final BlockService blockService;
    private final FollowRequestRepositorty followRequestRepositorty;
    private final FollowMapper mapper;

    @Autowired
    public FollowServices(
            FollowRepository followRepository, User_service userService, BlockService blockService,
            FollowRequestRepositorty followRequestRepositorty, FollowMapper mapper
    ){
        this.followRepository = followRepository;
        this.userService = userService;
        this.blockService = blockService;
        this.followRequestRepositorty = followRequestRepositorty;
        this.mapper = mapper;
    }

    @Transactional
    public FollowDTO createFollow(FollowDTO followDTO){
        boolean isBlockerFollower = blockService.isBlocked(
                followDTO.getFollowerId(),
                followDTO.getFollowingId()
        );
        boolean isBlockerFollowing = blockService.isBlocked(
                followDTO.getFollowingId(),
                followDTO.getFollowerId()
        );

        if(isBlockerFollower || isBlockerFollowing);


        userService.existByUser(
                followDTO.getFollowerId()
        );

        boolean isTargetUserPrivate = userService.isPrivateProfile(
                followDTO.getFollowingId()
        );

        alreadyExistsFollow(followDTO);

        if(followDTO.getFollowerId().equals(followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot follow yourself");
        }

        FollowStatus status = FollowStatus.ACCEPTED;
        if(isTargetUserPrivate){
            status = FollowStatus.PENDING;
        }


        followDTO.setFollowed(true);
        Follow follow = new Follow();
        follow.setFollowerId(followDTO.getFollowerId());
        follow.setFollowingId(followDTO.getFollowingId());
        follow.setStatus(status);
        followRepository.save(follow);
        return followDTO;
    }

    /*
    @Transactional
    public FollowDTO unFollow(FollowDTO followDTO){

        if(!userRepository.existsById(followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Following User Not Found");
        }
        if(!userRepository.existsById(followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Followed User Not Found");
        }
        if(followDTO.getFollowingId().equals(followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot unfollow yourself");
        }
        if(!followRepository.existsByFollowedIdAndFollowingId(followDTO.getFollowingId(), followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"You are not following this user");
        }

        followDTO.setFollowed(false);
        Optional<FollowSendRequest> optionalRequest = followRequestRepositorty.findByRequesterIdAndRequestedIdAndStatus(
                followDTO.getFollowingId(),
                followDTO.getFollowingId(),
                FollowRequestStatus.ACCEPTED
        );

        if (optionalRequest.isPresent()){
            FollowSendRequest request;
            request = optionalRequest.get();
            request.setStatus(FollowRequestStatus.UNFOLLOW);
            followRequestRepositorty.save(request);
        }

        followRepository.deleteByFollowedIdAndFollowingId(followDTO.getFollowingId(), followDTO.getFollowingId());

        return followDTO;
    }

    @Transactional
    public FollowDTO removeFollower(FollowDTO followDTO){

        if(!userRepository.existsById(followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Following User Not Found");
        }
        if(!userRepository.existsById(followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Followed User Not Found");
        }
        if(followDTO.getFollowingId().equals(followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot unfollow yourself");
        }
        if(!followRepository.existsByFollowedIdAndFollowingId(followDTO.getFollowingId(), followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"You are not following this user");
        }

        followDTO.setFollowed(false);
        Optional<FollowSendRequest> optionalRequest = followRequestRepositorty.findByRequesterIdAndRequestedIdAndStatus(
                followDTO.getFollowingId(),
                followDTO.getFollowingId(),
                FollowRequestStatus.ACCEPTED
        );

        if (optionalRequest.isPresent()){
            FollowSendRequest request;
            request = optionalRequest.get();
            request.setStatus(FollowRequestStatus.UNFOLLOW);
            followRequestRepositorty.save(request);
        }

        followRepository.deleteByFollowedIdAndFollowingId(followDTO.getFollowingId(), followDTO.getFollowingId());

        return followDTO;
    }



    @Transactional
    public List<FollowUserResponseDTO> userFollowing(Long id, int page, int size){
        userService.existByUser(
                id
        );
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        List<Long> followingList = followRepository
                .findByFollowerIdAndStatus(
                        id,
                        FollowStatus.ACCEPTED,
                        pageable
                )
                .stream().map(Follow::getFollowingId)
                .toList();

        if(followingList.isEmpty()){
            return Collections.emptyList();
        }


        return lastModified(
                userPage.getContent(),
                id
        );
    }

    @Transactional
    public List<FollowUserResponseDTO> userFollowed(Long id, int page, int size){
        if(!userRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User Not Found");
        }
        List<Long> followedList = followRepository.findByFollowedId(id)
                .stream().map(Follow::getFollowingId)
                .toList();


        if(followedList.isEmpty()){
            return Collections.emptyList();
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAllByIdIn(followedList,pageable);

        return lastModified(
                userPage.getContent(),
                id
        );
    }

     */

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

        Map<Long, FollowUserView> followingIds = followPage
                .getContent()
                .stream()
                .collect(Collectors.toMap(
                        FollowUserView::getUserId,
                        it-> it
                ));

        List<User> users = userService.getAllByIdIn(followingIds.keySet().stream().toList());

        return users
                .stream()
                .map(user -> {
                    return mapper.toFollowResponse(
                            user,
                            followingIds.get(user.getId())
                    );
                })
                .toList();
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

        Map<Long, FollowUserView> followingIds = followPage
                .getContent()
                .stream()
                .collect(Collectors.toMap(
                        FollowUserView::getUserId,
                        it-> it,
                        (existing, replacement) -> existing
                ));

        List<User> users = userService.getAllByIdIn(followingIds.keySet().stream().toList());

        return users
                .stream()
                .map(user -> {
                    return mapper.toFollowResponse(
                            user,
                            followingIds.get(user.getId())
                    );
                })
                .toList();
    }


    private void alreadyExistsFollow(FollowDTO followDTO){
        if(followRepository.existsByFollowerIdAndFollowingId(followDTO.getFollowingId(), followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Following this user");
        }
    }


}
