package com.beem.beem_sunucu.Follow;

import com.beem.beem_sunucu.Follow.FollowRequest.FollowRequestRepositorty;
import com.beem.beem_sunucu.Follow.FollowRequest.FollowRequestStatus;
import com.beem.beem_sunucu.Follow.FollowRequest.FollowResponseDTO;
import com.beem.beem_sunucu.Follow.FollowRequest.FollowSendRequest;
import com.beem.beem_sunucu.Users.User;
import com.beem.beem_sunucu.Users.User_Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final User_Repo userRepository;
    private final FollowRequestRepositorty followRequestRepositorty;
    private final FollowMapper mapper;

    @Autowired
    public FollowServices(
            FollowRepository followRepository,
            User_Repo userRepository,
            FollowRequestRepositorty followRequestRepositorty, FollowMapper mapper
    ){
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.followRequestRepositorty = followRequestRepositorty;
        this.mapper = mapper;
    }

    @Transactional
    public FollowDTO createFollow(FollowDTO followDTO){
        if(!userRepository.existsById(followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Following User Not Found");
        }
        if(!userRepository.existsById(followDTO.getFollowedId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Followed User Not Found");
        }
        if(followRepository.existsByFollowedIdAndFollowingId(followDTO.getFollowedId(), followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Following this user");
        }
        if(followDTO.getFollowingId().equals(followDTO.getFollowedId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot follow yourself");
        }

        followDTO.setFollowed(true);
        Follow follow = new Follow();
        follow.setFollowedId(followDTO.getFollowedId());
        follow.setFollowingId(followDTO.getFollowingId());
        followRepository.save(follow);
        return followDTO;
    }

    @Transactional
    public FollowDTO unFollow(FollowDTO followDTO){

        if(!userRepository.existsById(followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Following User Not Found");
        }
        if(!userRepository.existsById(followDTO.getFollowedId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Followed User Not Found");
        }
        if(followDTO.getFollowingId().equals(followDTO.getFollowedId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot unfollow yourself");
        }
        if(!followRepository.existsByFollowedIdAndFollowingId(followDTO.getFollowedId(), followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"You are not following this user");
        }

        followDTO.setFollowed(false);
        Optional<FollowSendRequest> optionalRequest = followRequestRepositorty.findByRequesterIdAndRequestedIdAndStatus(
                followDTO.getFollowingId(),
                followDTO.getFollowedId(),
                FollowRequestStatus.ACCEPTED
        );

        if (optionalRequest.isPresent()){
            FollowSendRequest request;
            request = optionalRequest.get();
            request.setStatus(FollowRequestStatus.UNFOLLOW);
            followRequestRepositorty.save(request);
        }

        followRepository.deleteByFollowedIdAndFollowingId(followDTO.getFollowedId(), followDTO.getFollowingId());

        return followDTO;
    }

    @Transactional
    public FollowDTO removeFollower(FollowDTO followDTO){

        if(!userRepository.existsById(followDTO.getFollowingId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Following User Not Found");
        }
        if(!userRepository.existsById(followDTO.getFollowedId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Followed User Not Found");
        }
        if(followDTO.getFollowingId().equals(followDTO.getFollowedId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot unfollow yourself");
        }
        if(!followRepository.existsByFollowedIdAndFollowingId(followDTO.getFollowingId(), followDTO.getFollowedId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"You are not following this user");
        }

        followDTO.setFollowed(false);
        Optional<FollowSendRequest> optionalRequest = followRequestRepositorty.findByRequesterIdAndRequestedIdAndStatus(
                followDTO.getFollowingId(),
                followDTO.getFollowedId(),
                FollowRequestStatus.ACCEPTED
        );

        if (optionalRequest.isPresent()){
            FollowSendRequest request;
            request = optionalRequest.get();
            request.setStatus(FollowRequestStatus.UNFOLLOW);
            followRequestRepositorty.save(request);
        }

        followRepository.deleteByFollowedIdAndFollowingId(followDTO.getFollowingId(), followDTO.getFollowedId());

        return followDTO;
    }



    @Transactional
    public List<FollowUserResponseDTO> userFollowing(Long id, int page, int size){

        if(!userRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User Not Found");
        }
        List<Long> followingList = followRepository.findByFollowingId(id)
                .stream().map(Follow::getFollowedId)
                .toList();

        if(followingList.isEmpty()){
            return Collections.emptyList();
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userRepository.findAllByIdIn(followingList,pageable);


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

    @Transactional
    public List<FollowUserResponseDTO> otherUserFollowing(Long myid, Long targetid, int page, int size){
        if(!userRepository.existsById(targetid)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User Not Found");
        }

        List<Long> targetUserfollowingList = followRepository.findByFollowingId(targetid).stream().map(Follow::getFollowedId).toList();
        if(targetUserfollowingList.isEmpty()){
            return Collections.emptyList();
        }

        Pageable pageable = PageRequest.of(page,size);

        Page<User> userPage = userRepository.findAllByIdIn(targetUserfollowingList, pageable);

        return lastModified(
                userPage.getContent(),
                myid
        );
    }

    @Transactional
    public List<FollowUserResponseDTO> otherUserFollowed(Long myid, Long targetid, int page, int size){
        if(!userRepository.existsById(targetid)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User Not Found");
        }

        List<Long> targetUserfollowedList = followRepository.findByFollowedId(targetid).stream().map(Follow::getFollowingId).toList();
        if(targetUserfollowedList.isEmpty()){
            return Collections.emptyList();
        }

        Pageable pageable = PageRequest.of(page,size);

        Page<User> userPage = userRepository.findAllByIdIn(targetUserfollowedList, pageable);

        return lastModified(
                userPage.getContent(),
                myid
        );

    }

    private Map<Long ,FollowSendRequest> myPendingRequest(List<User> userList, Long id){
        Set<Long> filter = extractUserIds(userList);
        return
                toRequestMapFollowing(
                        followRequestRepositorty.findAllOrderByStatusPriority(
                                id,
                                filter
                        )
                );
    }

    private Set<Long> extractUserIds(List<User> users) {
        return users.stream()
                .map(User::getId)
                .collect(Collectors.toSet());
    }


    private Map<Long ,FollowSendRequest> getMyFollowersRequests(List<User> userList, Long id){
        Set<Long> filter = extractUserIds(userList);

        return
                toRequestMapFollowers(followRequestRepositorty
                    .findAllOrderByStatusPriorityFollowers(
                            id,
                            filter
                    ));
    }

    private Map<Long, FollowSendRequest> toRequestMapFollowing(List<FollowSendRequest> requests) {
        return requests.stream()
                .collect(Collectors.toMap(
                        r -> r.getRequested().getId(),
                        Function.identity(),
                        (existing, ignored) -> existing
                ));
    }
    private Map<Long, FollowSendRequest> toRequestMapFollowers(List<FollowSendRequest> requests) {
        return requests.stream()
                .collect(Collectors.toMap(
                        r -> r.getRequester().getId(),
                        Function.identity(),
                        (existing, ignored) -> existing
                ));
    }


    private FollowUserResponseDTO mappedFollowUserResponseDTO(
            User user,
            Map<Long, FollowSendRequest> myPendingRequest,
            Map<Long ,FollowSendRequest> getMyFollowersRequests
    ){
        FollowResponseDTO dto = new FollowResponseDTO();

        FollowSendRequest myFollow = myPendingRequest.get(user.getId());
        FollowSendRequest follower = getMyFollowersRequests.get(user.getId());

        boolean myFollower = follower != null;
        boolean myFollowOrPending = myFollow != null;

        if(myFollowOrPending){
            return mapper.toFollowUserResponseDTO(
                    user,
                    myFollow,
                    myFollower,
                    myFollowOrPending
            );
        }

        return mapper.toFollowUserResponseDTO(
                user,
                follower,
                myFollower,
                myFollowOrPending
        );
    }


    private List<FollowUserResponseDTO> lastModified(List<User> userList, Long id){
        Map<Long, FollowSendRequest> myPendingRequest = myPendingRequest(
                userList,
                id
        );

        Map<Long, FollowSendRequest> myFollowRequest = getMyFollowersRequests(
                userList,
                id
        );

        return userList
                .stream().map(user -> {
                    return mappedFollowUserResponseDTO(
                            user,
                            myPendingRequest,
                            myFollowRequest
                    );
                })
                .toList();
    }


}
