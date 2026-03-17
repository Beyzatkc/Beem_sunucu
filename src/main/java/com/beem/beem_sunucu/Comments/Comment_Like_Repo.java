package com.beem.beem_sunucu.Comments;

import com.beem.beem_sunucu.Posts.Post_Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface Comment_Like_Repo extends JpaRepository<Comment_Like,Long> {
    List<Comment_Like> findByComment_CommentIdInAndUser_Id(List<Long> commentIds, Long userId);

    Optional<Comment_Like> findByComment_CommentIdAndUser_Id(Long commentId,Long userId);

    boolean existsByComment_CommentIdAndUser_Id(Long commentId, Long userId);
    @Query(
            value = """
        SELECT cl.*
        FROM comment_like cl
        JOIN users u ON cl.user_id = u.id
        LEFT JOIN follows t 
        ON t.following_id = :currentUserId AND t.followed_id = u.id
        WHERE cl.comment_id = :commentId
        ORDER BY 
        CASE WHEN t.followed_id IS NOT NULL THEN 0 ELSE 1 END,
        cl.comment_likes_id DESC
        """,
            countQuery = "SELECT COUNT(*) FROM comment_like WHERE comment_id = :commentId",
            nativeQuery = true
    )
    Page<Comment_Like> findCommentLikesWithFollowOrder(
            @Param("commentId") Long postId,
            @Param("currentUserId") Long currentUserId,
            Pageable pageable
    );


}
