package com.beem.beem_sunucu.Follow.FollowRequest;


import com.beem.beem_sunucu.Users.User_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow/request")
public class FollowRequestController {

    private final FollowRequestService service;
    private final User_service userService;

    @Autowired
    public FollowRequestController(FollowRequestService service, User_service userService){
        this.service = service;
        this.userService = userService;
    }

    @PostMapping
    FollowResponseDTO sendFollowRequest(@RequestBody FollowRequestDTO requestDTO){
        userService.securityUser(requestDTO.getRequesterId());
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

    @PutMapping("/{id}/cancel")
    public FollowResponseDTO cancelFollowRequest(@PathVariable Long id) {
        return service.cancelRequest(id);
    }

    @GetMapping("/pending")
    public List<FollowResponseDTO> getPendingRequests() {
        Long requestedId = userService.getCurrentUserId();
        return service.getPendingRequests(requestedId);
    }

    @GetMapping("/sent")
    public List<FollowResponseDTO> getSentRequests() {
        Long requesterId = userService.getCurrentUserId();
        return service.getSentRequests(requesterId);
    }
}
