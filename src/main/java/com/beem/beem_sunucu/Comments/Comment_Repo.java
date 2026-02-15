package com.beem.beem_sunucu.Comments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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
        WHERE p.postId = :postId
        AND c.parentComment IS NULL
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


    Page<Comment>findByParentComment_CommentIdOrderByCommentDateDesc(Long parentCommentId,Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Comment c SET c.numberofLikes = :numberOfLikes WHERE c.commentId = :commentId")
    void updateLikeCount(@Param("commentId") Long commentId, @Param("numberOfLikes") int numberOfLikes);
}

