package com.beem.beem_sunucu.Follow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findByFollowedId(Long followedId);

    List<Follow> findByFollowingId(Long following);

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);


    void deleteByFollowedIdAndFollowingId(Long followedId, Long followingId);

    Long countByFollowedId(Long followedId);

    Long countByFollowingId(Long following);
    @Query("SELECT t.followedId FROM Follow t WHERE t.followingId = :userId")
    List<Long> findFollowedIds(@Param("userId") Long userId);

}
