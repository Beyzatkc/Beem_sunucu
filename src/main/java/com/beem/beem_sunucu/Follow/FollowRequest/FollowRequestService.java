package com.beem.beem_sunucu.Follow.FollowRequest;


import com.beem.beem_sunucu.Follow.Follow;
import com.beem.beem_sunucu.Follow.FollowRepository;
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


    @Autowired
    public FollowRequestService(FollowRequestRepositorty followRequestRepositorty,
                                FollowRepository followRepository){
        this.followRequestRepositorty = followRequestRepositorty;
        this.followRepository = followRepository;
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

        FollowSendRequest request = new FollowSendRequest(requestDTO);
        followRequestRepositorty.save(request);

        return new FollowResponseDTO(request);

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
        followRepository.save(new Follow(request));

        return new FollowResponseDTO(
                request.getId(),
                request.getRequesterId(),
                request.getRequestedId(),
                request.getStatus(),
                request.getDate()
        );
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

        return new FollowResponseDTO(
                request.getId(),
                request.getRequesterId(),
                request.getRequestedId(),
                request.getStatus(),
                request.getDate()
        );
    }

    @Transactional
    public List<FollowResponseDTO> getPendingRequests(Long requestedId){
        return followRequestRepositorty.findByRequestedIdAndStatus(requestedId, FollowRequestStatus.PENDING).stream()
                .map(request -> new FollowResponseDTO(request)).collect(Collectors.toList());
    }

    @Transactional
    public List<FollowResponseDTO> getSentRequests(Long requesterId) {
        return followRequestRepositorty.findByRequesterId(requesterId)
                .stream()
                .map(request -> new FollowResponseDTO(request))
                .collect(Collectors.toList());
    }
}
