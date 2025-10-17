package com.beem.beem_sunucu.Block;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/block")
public class BlockController {

    private final BlockService blockService;

    @Autowired
    public BlockController(BlockService blockService) {
        this.blockService = blockService;
    }

    @PostMapping("/{blockerId}/{blockedId}")
    public ResponseEntity<String> blockUser(@PathVariable Long blockerId, @PathVariable Long blockedId) {
        blockService.blockUser(blockerId, blockedId);
        return ResponseEntity.ok("User blocked successfully.");
    }

    @DeleteMapping("/{blockerId}/{blockedId}")
    public ResponseEntity<String> unblockUser(@PathVariable Long blockerId, @PathVariable Long blockedId) {
        blockService.unblockUser(blockerId, blockedId);
        return ResponseEntity.ok("User unblocked successfully.");
    }

    @GetMapping("/{blockerId}")
    public ResponseEntity<List<BlockResponseDTO>> getBlockedUsers(@PathVariable Long blockerId) {
        return ResponseEntity.ok(blockService.getBlockedUsers(blockerId));
    }
}
