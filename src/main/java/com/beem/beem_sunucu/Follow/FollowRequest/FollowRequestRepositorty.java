package com.beem.beem_sunucu.Follow.FollowRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRequestRepositorty extends JpaRepository<FollowSendRequest,Long> {

    boolean existsByRequesterIdAndRequestedId(Long requesterId, Long requestedId);
    // Status kontrollusunu yap

    List<FollowSendRequest> findByRequestedIdAndStatus(Long requestedId, FollowRequestStatus status);

    List<FollowSendRequest> findByRequesterId(Long requesterId);
}
