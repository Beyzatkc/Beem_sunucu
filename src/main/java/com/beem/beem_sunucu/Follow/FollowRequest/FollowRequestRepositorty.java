package com.beem.beem_sunucu.Follow.FollowRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRequestRepositorty extends JpaRepository<FollowSendRequest,Long> {

    boolean existsByRequesterIdAndRequestedIdAndStatusIn(Long requesterId, Long requestedId,List<FollowRequestStatus> statuses);

    Optional<FollowSendRequest> findByRequesterIdAndRequestedIdAndStatus(Long requesterId, Long requestedId, FollowRequestStatus statuses);

    List<FollowSendRequest> findByRequestedIdAndStatus(Long requestedId, FollowRequestStatus status);

    List<FollowSendRequest> findByRequesterId(Long requesterId);
}
