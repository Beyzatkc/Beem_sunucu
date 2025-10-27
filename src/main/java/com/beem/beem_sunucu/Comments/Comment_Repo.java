package com.beem.beem_sunucu.Comments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface Comment_Repo extends JpaRepository<Comment,Long> {
    Page<Comment>findByPost_PostIdAndParentCommentIsNullOrderByCommentDateDesc(Long postId, Pageable pageable);
    Page<Comment>findByParentComment_CommentIdOrderByCommentDateDesc(Long parentCommentId,Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Comment c SET c.numberofLikes = :numberOfLikes WHERE c.commentId = :commentId")
    void updateLikeCount(@Param("commentId") Long commentId, @Param("numberOfLikes") int numberOfLikes);
}

