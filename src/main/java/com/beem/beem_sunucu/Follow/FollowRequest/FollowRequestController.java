package com.beem.beem_sunucu.Follow.FollowRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow/request")
public class FollowRequestController {

    private final FollowRequestService service;

    @Autowired
    public FollowRequestController(FollowRequestService service){
        this.service = service;
    }

    @PostMapping
    FollowResponseDTO sendFollowRequest(@RequestBody FollowRequestDTO requestDTO){
        return service.sendRequest(requestDTO);
    }

    @PutMapping("/{id}/accept")
    public FollowResponseDTO acceptFollowRequest(@PathVariable Long id) {
        return service.acceptRequest(id);
    }

    @PutMapping("/{id}/reject")
    public FollowResponseDTO rejectFollowRequest(@PathVariable Long id) {
        return service.rejectRequest(id);
    }

    @GetMapping("/pending/{requestedId}")
    public List<FollowResponseDTO> getPendingRequests(@PathVariable Long requestedId) {
        return service.getPendingRequests(requestedId);
    }

    @GetMapping("/sent/{requesterId}")
    public List<FollowResponseDTO> getSentRequests(@PathVariable Long requesterId) {
        return service.getSentRequests(requesterId);
    }
}
