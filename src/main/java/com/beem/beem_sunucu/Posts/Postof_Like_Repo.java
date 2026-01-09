package com.beem.beem_sunucu.Posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

interface Postof_Like_Repo extends JpaRepository<Post_Like,Long> {
    Optional<Post_Like> findByPost_PostIdAndUser_Id(Long postId, Long userId);

    @Query("SELECT pl.post.postId FROM Post_Like pl WHERE pl.user.id = :userId")
    List<Long> findPostIdsByUserId(@Param("userId") Long userId);


    @Query(
            value = """
        SELECT pl.*
        FROM post_likes pl
        JOIN users u ON pl.user_id = u.id
        LEFT JOIN follows t 
        ON t.following_id = :currentUserId AND t.followed_id = u.id
        WHERE pl.post_id = :postId
        ORDER BY 
        CASE WHEN t.followed_id IS NOT NULL THEN 0 ELSE 1 END,
        pl.post_likes_id DESC
        """,
            countQuery = "SELECT COUNT(*) FROM post_likes WHERE post_id = :postId",
            nativeQuery = true
    )
    Page<Post_Like> findPostLikesWithFollowOrder(
            @Param("postId") Long postId,
            @Param("currentUserId") Long currentUserId,
            Pageable pageable
    );


    @Query("SELECT DISTINCT pl.post.postId FROM Post_Like pl WHERE pl.user.id IN :followIds")
    List<Long> findPostIdsByUsers(@Param("followIds") List<Long> followIds);

}
