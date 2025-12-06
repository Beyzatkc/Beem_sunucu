package com.beem.beem_sunucu.Block;

import com.beem.beem_sunucu.Users.User_service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/block")
public class BlockController {

    private final User_service userService;
    private final BlockService blockService;

    @Autowired
    public BlockController(User_service userService, BlockService blockService) {
        this.userService = userService;
        this.blockService = blockService;
    }

    @PostMapping("/{blockedId}")
    public ResponseEntity<String> blockUser(@PathVariable Long blockedId) {
        Long blockerId = userService.getCurrentUserId();
        blockService.blockUser(blockerId, blockedId);
        return ResponseEntity.ok("User blocked successfully.");
    }

    @DeleteMapping("/{blockedId}")
    public ResponseEntity<String> unblockUser(@PathVariable Long blockedId) {
        Long blockerId = userService.getCurrentUserId();
        blockService.unblockUser(blockerId, blockedId);
        return ResponseEntity.ok("User unblocked successfully.");
    }

    @GetMapping("/getBlock")
    public ResponseEntity<List<BlockResponseDTO>> getBlockedUsers() {
        Long blockerId = userService.getCurrentUserId();
        return ResponseEntity.ok(blockService.getBlockedUsers(blockerId));
    }
}
