package com.beem.beem_sunucu.Block;

import com.beem.beem_sunucu.Follow.Follow;
import com.beem.beem_sunucu.Follow.FollowRepository;
import com.beem.beem_sunucu.Follow.FollowRequest.FollowRequestRepositorty;
import com.beem.beem_sunucu.Follow.FollowRequest.FollowRequestStatus;
import com.beem.beem_sunucu.Follow.FollowRequest.FollowSendRequest;
import com.beem.beem_sunucu.Users.User;
import com.beem.beem_sunucu.Users.User_Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@Service
public class BlockService {

    private final BlockRepository blockRepository;
    private final User_Repo userRepo;
    private final FollowRepository followRepo;
    private final FollowRequestRepositorty followRequestRepo;

    @Autowired
    public BlockService(BlockRepository blockRepository,User_Repo userRepo,
                        FollowRepository followRepo,
                        FollowRequestRepositorty followRequestRepo){
        this.blockRepository = blockRepository;
        this.userRepo = userRepo;
        this.followRepo = followRepo;
        this.followRequestRepo = followRequestRepo;
    }

    @Transactional
    public void blockUser(Long blockerId, Long blockedId) {
        if (blockerId.equals(blockedId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot block yourself.");
        }
        if (blockRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already blocked.");
        }

        User blocker = userRepo.findById(blockerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blocker not found"));
        User blocked = userRepo.findById(blockedId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Blocked user not found"));

        BlockedRequest(blockerId,blockedId);
        BlockedRequest(blockedId,blockerId);

        Block block = new Block(blocker, blocked);
        blockRepository.save(block);
    }

    @Transactional
    public void unblockUser(Long blockerId, Long blockedId) {
        Block block = blockRepository.findByBlockerIdAndBlockedId(blockerId, blockedId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Block not found"));

        blockRepository.delete(block);
    }

    public List<BlockResponseDTO> getBlockedUsers(Long blockerId) {
        return blockRepository.findByBlockerId(blockerId)
                .stream()
                .map(block -> new BlockResponseDTO(block))
                .toList();
    }

    public boolean isBlocked(Long blockerId, Long blockedId) {
        return blockRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId);
    }

    private void BlockedRequest(Long id, Long targetId){
        followRequestRepo.findByRequesterIdAndRequestedIdAndStatus(id, targetId, FollowRequestStatus.ACCEPTED)
                .ifPresent(request -> {
                    request.setStatus(FollowRequestStatus.BLOCKED);
                    followRequestRepo.save(request);
                    followRepo.deleteByFollowedIdAndFollowingId(targetId, id);
                });
    }
}
