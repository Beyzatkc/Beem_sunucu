package com.beem.beem_sunucu.Block;

import com.beem.beem_sunucu.Follow.FollowServices;
import com.beem.beem_sunucu.Users.User;
import com.beem.beem_sunucu.Users.User_Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class BlockService {

    private final BlockRepository blockRepository;
    private final User_Repo userRepo;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public BlockService(BlockRepository blockRepository,
                        User_Repo userRepo,
                        ApplicationEventPublisher eventPublisher
    ){
        this.blockRepository = blockRepository;
        this.userRepo = userRepo;
        this.eventPublisher = eventPublisher;
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

        eventPublisher.publishEvent(new UserBlockedEvent(blockedId, blockerId));

        Block block = new Block(blocker, blocked);
        blockRepository.save(block);
    }

    @Transactional
    public void unblockUser(Long blockerId, Long blockedId) {
        Block block = blockRepository.findByBlockerIdAndBlockedId(blockerId, blockedId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Block not found"));

        blockRepository.delete(block);
    }

    protected List<BlockResponseDTO> getBlockedUsers(Long blockerId) {
        return blockRepository.findByBlockerId(blockerId)
                .stream()
                .map(block -> new BlockResponseDTO(block))
                .toList();
    }


    public boolean isBlocked(Long blockerId, Long blockedId) {
        return blockRepository.existsByBlockerIdAndBlockedId(blockerId, blockedId);
    }
}
