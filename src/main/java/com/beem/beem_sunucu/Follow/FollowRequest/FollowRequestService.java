package com.beem.beem_sunucu.Follow.FollowRequest;


import com.beem.beem_sunucu.Follow.Follow;
import com.beem.beem_sunucu.Follow.FollowRepository;
import com.beem.beem_sunucu.Users.SimpleUserDTO;
import com.beem.beem_sunucu.Users.User;
import com.beem.beem_sunucu.Users.User_Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowRequestService {

    private final FollowRequestRepositorty followRequestRepositorty;
    private final FollowRepository followRepository;
    private final User_Repo userRepo;


    @Autowired
    public FollowRequestService(FollowRequestRepositorty followRequestRepositorty,
                                FollowRepository followRepository,
                                User_Repo userRepo
                                ){
        this.followRequestRepositorty = followRequestRepositorty;
        this.followRepository = followRepository;
        this.userRepo = userRepo;
    }

    @Transactional
    public FollowResponseDTO sendRequest(FollowRequestDTO requestDTO){
        if(followRequestRepositorty.existsByRequesterIdAndRequestedIdAndStatusIn(
                requestDTO.getRequesterId(),
                requestDTO.getRequestedId(),
                List.of(FollowRequestStatus.ACCEPTED, FollowRequestStatus.PENDING))
        ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"The request already exists.");
        }

        FollowSendRequest request = new FollowSendRequest(
                findUser(requestDTO.getRequesterId()),
                findUser(requestDTO.getRequestedId())
        );
        FollowSendRequest newRequest = followRequestRepositorty.save(request);

        return flowFollowRequest(newRequest, true);

    }

    @Transactional
    public FollowResponseDTO acceptRequest(Long requestId) {
        FollowSendRequest request = followRequestRepositorty.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The request not found."));

        if (request.getStatus() != FollowRequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The request already exists");
        }

        request.setStatus(FollowRequestStatus.ACCEPTED);
        followRequestRepositorty.save(request);

        return flowFollowRequest(request, false);
    }

    @Transactional
    public FollowResponseDTO rejectRequest(Long requestId) {
        FollowSendRequest request = followRequestRepositorty.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));

        if (request.getStatus() != FollowRequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The request already exists");
        }

        request.setStatus(FollowRequestStatus.REJECTED);
        followRequestRepositorty.save(request);

        return flowFollowRequest(request, false);
    }

    @Transactional
    public FollowResponseDTO cancelRequest(Long requestId) {
        FollowSendRequest request = followRequestRepositorty.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));

        if (request.getStatus() != FollowRequestStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The request already exists");
        }

        request.setStatus(FollowRequestStatus.CANCEL);
        followRequestRepositorty.save(request);

        return flowFollowRequest(request, true);
    }

    @Transactional
    public List<FollowResponseDTO> getPendingRequests(Long requestedId){
        return followRequestRepositorty.findByRequestedIdAndStatus(requestedId, FollowRequestStatus.PENDING).stream()
                .map(request -> new FollowResponseDTO(
                        request,
                        new SimpleUserDTO(request.getRequester()),
                        new SimpleUserDTO(request.getRequested())
                )).collect(Collectors.toList());
    }

    @Transactional
    public List<FollowResponseDTO> getSentRequests(Long requesterId) {
        return followRequestRepositorty.findByRequesterId(requesterId)
                .stream()
                .map(request -> new FollowResponseDTO(
                        request,
                        new SimpleUserDTO(request.getRequester()),
                        new SimpleUserDTO(request.getRequested())
                ))
                .collect(Collectors.toList());
    }

    private User findUser(Long id){
        return userRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private FollowResponseDTO flowFollowRequest(FollowSendRequest request, boolean flowType){
        boolean isMyFollower = isFollowing(
                request.getRequester().getId(),
                request.getRequested().getId()
        );
        boolean isFollowingYou = isFollowing(
                request.getRequested().getId(),
                request.getRequester().getId()
        );

        boolean tempBool = isMyFollower;

        isMyFollower = flowType ? isFollowingYou: tempBool;
        isFollowingYou =  flowType ? tempBool: isFollowingYou;

        return new FollowResponseDTO(
                request.getId(),
                new SimpleUserDTO(request.getRequester()),
                new SimpleUserDTO(request.getRequested()),
                request.getStatus(),
                request.getDate(),
                isMyFollower,
                isFollowingYou
        );
    }

    public boolean isFollowing(Long requesterId, Long requestedId) {
        return followRequestRepositorty.isFollowingRaw(requesterId, requestedId) > 0;
    }


}
