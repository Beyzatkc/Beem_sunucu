package com.beem.beem_sunucu.Follow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findByFollowedId(Long followedId);

    List<Follow> findByFollowingId(Long following);

    boolean existsByFollowedIdAndFollowingId(Long followedId, Long followingId);


    void deleteByFollowedIdAndFollowingId(Long followedId, Long followingId);

    Long countByFollowedId(Long followedId);

    Long countByFollowingId(Long following);
}
