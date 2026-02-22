package com.beem.beem_sunucu.Comments;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface Comment_Repo extends JpaRepository<Comment,Long> {
    @Query(
            value = """
    SELECT new com.beem.beem_sunucu.Comments.Comment_DTO_Response(
        c.commentId,
        p.postId,
        p.user.id,
        u.id,
        u.username,
        c.contents,
        c.numberofLikes,
        c.commentDate,
        c.updateDate,
        parent.commentId,
        c.isPinned
    )
    FROM Comment c
    JOIN c.post p
    JOIN c.user u
    LEFT JOIN c.parentComment parent
    LEFT JOIN c.subComments sc
    WHERE p.postId = :postId
    AND c.parentComment IS NULL
    GROUP BY c.commentId, p.postId, p.user.id, 
             u.id, u.username,
             c.contents, c.numberofLikes,
             c.commentDate, c.updateDate,
             parent.commentId, c.isPinned
    ORDER BY c.isPinned DESC, c.commentDate DESC
""",
            countQuery = """
    SELECT COUNT(c)
    FROM Comment c
    WHERE c.post.postId = :postId
    AND c.parentComment IS NULL
"""
    )
    Page<Comment_DTO_Response> findMainComments(
            @Param("postId") Long postId,
            Pageable pageable
    );

    @Query("""
SELECT COUNT(c)
FROM Comment c
WHERE c.parentComment.commentId = :parentId
""")
    long countSubComments(@Param("parentId") Long parentId);


    @Query("""
    SELECT COUNT(c)
    FROM Comment c
    WHERE c.post.postId = :postId
    AND c.parentComment IS NULL
    AND c.isPinned = true
""")
    Long countPinnedComments(@Param("postId") Long postId);


        @Query(
                value = """
            SELECT new com.beem.beem_sunucu.Comments.Comment_DTO_Response(
                c.commentId,
                p.postId,
                p.user.id,
                u.id,
                u.username,
                c.contents,
                c.numberofLikes,
                c.commentDate,
                c.updateDate,
                parent.commentId,
                c.isPinned
            )
            FROM Comment c
            JOIN c.post p
            JOIN c.user u
            JOIN c.parentComment parent
            WHERE parent.commentId = :parentCommentId
            ORDER BY c.commentDate ASC
        """,
                countQuery = """
            SELECT COUNT(c)
            FROM Comment c
            WHERE c.parentComment.commentId = :parentCommentId
        """
        )
        Page<Comment_DTO_Response> findSubCommentsByParentId(
                @Param("parentCommentId") Long parentCommentId,
                Pageable pageable
        );

}

