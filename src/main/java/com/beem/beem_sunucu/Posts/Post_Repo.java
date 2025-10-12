package com.beem.beem_sunucu.Posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Post_Repo extends JpaRepository<Post,Long> {
    Page<Post> findByKisiId(Long userId, Pageable pageable);
    @Modifying
    @Query("UPDATE Post p SET p.numberofLikes = :numberOfLikes WHERE p.postId = :postId")
    void updateLikeCount(@Param("postId") Long postId, @Param("numberOfLikes") int numberOfLikes);

    @Query("""
    SELECT p FROM Post p
    LEFT JOIN FETCH p.user u
    ORDER BY 
        p.postDate DESC,
        (CASE WHEN u.userId IN :followIds THEN 100 ELSE 0 END +
         CASE WHEN p.postId IN :followLikes THEN 70 ELSE 0 END +
         CASE WHEN p.numberofLikes > 50 THEN 30
              WHEN p.numberofLikes > 10 THEN 15
              ELSE 0 END +
         CASE WHEN u.userId NOT IN :followIds THEN 10 ELSE 0 END) DESC
    """)
    Page<Post> findHomePagePostsJPQL(
            @Param("followIds") List<Long> followIds,
            @Param("followLikes") List<Long> followLikes,
            Pageable pageable
    );
}

