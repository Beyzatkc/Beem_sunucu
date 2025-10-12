package com.beem.beem_sunucu.Follow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findByFollowedId(Long followedId);

    List<Follow> findByFollowingId(Long following);

    boolean existsByFollowedIdAndFollowingId(Long followedId, Long followingId);

    void deleteByFollowedIdAndFollowingId(Long followedId, Long followingId);

    @Query("SELECT t.followedId FROM Follow t WHERE t.followingId = :userId")
    List<Long> findFollowedIds(@Param("userId") Long userId);

}
