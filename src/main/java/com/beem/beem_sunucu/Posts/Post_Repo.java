package com.beem.beem_sunucu.Posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Post_Repo extends JpaRepository<Post,Long> {
    Page<Post> findByUser_Id(Long userId, Pageable pageable);
    @Modifying
    @Query("UPDATE Post p SET p.numberofLikes = :numberOfLikes WHERE p.postId = :postId")
    void updateLikeCount(@Param("postId") Long postId, @Param("numberOfLikes") int numberOfLikes);
}

