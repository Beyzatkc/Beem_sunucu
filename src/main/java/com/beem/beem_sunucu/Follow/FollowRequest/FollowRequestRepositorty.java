package com.beem.beem_sunucu.Follow.FollowRequest;

import com.beem.beem_sunucu.Users.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface FollowRequestRepositorty extends JpaRepository<FollowSendRequest,Long> {

    boolean existsByRequesterIdAndRequestedIdAndStatusIn(Long requesterId, Long requestedId,List<FollowRequestStatus> statuses);

    Optional<FollowSendRequest> findByRequesterIdAndRequestedIdAndStatus(Long requesterId, Long requestedId, FollowRequestStatus statuses);

    List<FollowSendRequest> findByRequestedIdAndStatus(Long requestedId, FollowRequestStatus status);

    List<FollowSendRequest> findByRequesterId(Long requesterId);

    List<FollowSendRequest> findByRequesterIdAndStatusAndRequestedIdIn(
            Long requesterId,
            FollowRequestStatus status,
            Set<Long> requestedIds
    );

    @Query(value = """
    SELECT *
    FROM followrequests
    WHERE requester_id = :requesterId
      AND requested_id IN (:requestedIds)
    ORDER BY
        CASE status
            WHEN 'PENDING'  THEN 1
            WHEN 'ACCEPTED' THEN 2
            ELSE 3
        END,
        date DESC
    """, nativeQuery = true)
    List<FollowSendRequest> findAllOrderByStatusPriority(
            @Param("requesterId") Long requesterId,
            @Param("requestedIds") Set<Long> requestedIds
    );
}
