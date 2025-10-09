package com.beem.beem_sunucu.Posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

interface Postof_Like_Repo extends JpaRepository<Post_Like,Long> {
    void deleteByPost_PostIdAndUser_Id(Long postId, Long userId);
    long countByPost_PostId(Long postId);
    Page<Post_Like> findByPost_PostId(Long postId, Pageable pageable);
}
