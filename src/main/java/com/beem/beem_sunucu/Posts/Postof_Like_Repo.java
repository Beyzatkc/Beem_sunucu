package com.beem.beem_sunucu.Posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface Postof_Like_Repo extends JpaRepository<Post_Like,Long> {
    void deleteByPost_PostIdAndUser_Id(Long postId, Long userId);
    long countByPost_PostId(Long postId);
    Page<Post_Like> findByPost_PostId(Long postId, Pageable pageable);
    @Query(value = """
    SELECT pl.*
    FROM postLikes pl
    JOIN users u ON pl.user_id = u.id
    LEFT JOIN follows t 
        ON t.following_id = :currentUserId AND t.followed_id = u.id
    WHERE pl.post_id = :postId
    ORDER BY 
        CASE WHEN t.followed_id IS NOT NULL THEN 0 ELSE 1 END,
        pl.id DESC
""", nativeQuery = true)
    Page<Post_Like> findLikesOrderedByFollow(@Param("postId") Long postId,
                                             @Param("currentUserId") Long currentUserId,
                                             Pageable pageable);
}
