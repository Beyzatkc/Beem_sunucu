package com.beem.beem_sunucu.Posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

interface Postof_Like_Repo extends JpaRepository<Post_Like,Long> {
    long countByPost_PostId(Long postId);
    Page<Post_Like> findByPost_PostId(Long postId, Pageable pageable);
    Optional<Post_Like> findByPost_PostIdAndUser_Id(Long postId, Long userId);

    @Query(value = """
    SELECT pl.*
    FROM postLikes pl
    JOIN users u ON pl.user_id = u.id
    LEFT JOIN follows t 
        ON t.following_id = :currentUserId AND t.followed_id = u.id
    WHERE pl.post_id = :postId
    ORDER BY 
        CASE WHEN t.followed_id IS NOT NULL THEN 0 ELSE 1 END,
        pl.post_likes_id DESC
    """, nativeQuery = true)
    List<Post_Like> findPostLikesWithFollowOrder(@Param("postId") Long postId,
                                                 @Param("currentUserId") Long currentUserId,Pageable pageable);


    @Query("SELECT DISTINCT pl.post.postId FROM Post_Like pl WHERE pl.user.id IN :followIds")
    List<Long> findPostIdsByUsers(@Param("followIds") List<Long> followIds);

}
