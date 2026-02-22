package com.beem.beem_sunucu.Follow;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findByFollowerId(Long follower);

    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);


    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Long countByFollowerId(Long followerId);

    Long countByFollowingId(Long following);

    @Query("SELECT t.followingId FROM Follow t WHERE t.followerId = :userId")
    List<Long> findFollowingIds(@Param("userId") Long userId);

    Page<Follow> findByFollowerIdAndStatus(
            Long followerId,
            FollowStatus status,
            Pageable pageable
    );



    @Query(value = """
    SELECT
        f.follower_id as userId,
        CASE WHEN myFollow.id IS NOT NULL THEN true ELSE false END AS isFollowing,
        CASE WHEN followsMe.id IS NOT NULL THEN true ELSE false END AS isFollower,
        CASE WHEN pending.id IS NOT NULL THEN true ELSE false END AS isPending
    FROM follows f
    LEFT JOIN follows myFollow
        ON myFollow.follower_id = :currentUserId
        AND myFollow.following_id = f.follower_id
        AND myFollow.status = 'ACCEPTED'
    LEFT JOIN follows followsMe
        ON followsMe.follower_id = f.follower_id
        AND followsMe.following_id = :currentUserId
        AND followsMe.status = 'ACCEPTED'
    LEFT JOIN follows pending
        ON pending.follower_id = :currentUserId
        AND pending.following_id = f.following_id
        AND pending.status = 'PENDING'
    WHERE f.following_id = :profileUserId
      AND f.status = 'ACCEPTED'
    ORDER BY
        CASE
            WHEN f.follower_id = :currentUserId THEN 1
            WHEN myFollow.id IS NOT NULL AND followsMe.id IS NOT NULL THEN 2
            WHEN followsMe.id IS NOT NULL THEN 3
            ELSE 4
        END,
        f.created_at DESC
    """,
            countQuery = """
        SELECT COUNT(*)
        FROM follows f
        WHERE f.following_id = :profileUserId
        AND f.status = 'ACCEPTED'
    """,
            nativeQuery = true
    )
    Page<FollowUserView> findFollowersWithPriority(
            @Param("profileUserId") Long profileUserId,
            @Param("currentUserId") Long currentUserId,
            Pageable pageable
    );


    @Query(value = """
    SELECT
        f.following_id as userId,
        CASE WHEN myFollow.id IS NOT NULL THEN true ELSE false END AS isFollowing,
        CASE WHEN followsMe.id IS NOT NULL THEN true ELSE false END AS isFollower,
        CASE WHEN pending.id IS NOT NULL THEN true ELSE false END AS isPending
    FROM follows f
    LEFT JOIN follows myFollow
        ON myFollow.follower_id = :currentUserId
        AND myFollow.following_id = f.following_id
        AND myFollow.status = 'ACCEPTED'
    LEFT JOIN follows followsMe
        ON followsMe.follower_id = f.following_id
        AND followsMe.following_id = :currentUserId
        AND followsMe.status = 'ACCEPTED'
    LEFT JOIN follows pending
        ON pending.follower_id = :currentUserId
        AND pending.following_id = f.following_id
        AND pending.status = 'PENDING'
    WHERE f.follower_id = :profileUserId
      AND f.status = 'ACCEPTED'
    ORDER BY
        CASE
            WHEN f.following_id = :currentUserId THEN 1
            WHEN myFollow.id IS NOT NULL AND followsMe.id IS NOT NULL THEN 2
            WHEN followsMe.id IS NOT NULL THEN 3
            ELSE 4
        END,
        f.created_at DESC
    """,
            countQuery = """
        SELECT COUNT(*)
        FROM follows f
        WHERE f.follower_id = :profileUserId
        AND f.status = 'ACCEPTED'
    """,
            nativeQuery = true
    )
    Page<FollowUserView> findFollowingWithPriority(
            @Param("profileUserId") Long profileUserId,
            @Param("currentUserId") Long currentUserId,
            Pageable pageable
    );

}
