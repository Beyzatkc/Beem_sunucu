package com.beem.beem_sunucu.Comments;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Comment_Repo extends JpaRepository<Comment,Long> {
    Page<Comment>findByPost_PostIdOrderByCommentDateDesc(Long postId, Pageable pageable);
}
