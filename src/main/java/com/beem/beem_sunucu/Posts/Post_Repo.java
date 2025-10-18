package com.beem.beem_sunucu.Posts;

import com.beem.beem_sunucu.Users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface Post_Repo extends JpaRepository<Post,Long> {
    Page<Post> findByUser_Id(Long userId, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Post p SET p.numberofLikes = :numberOfLikes WHERE p.postId = :postId")
    void updateLikeCount(@Param("postId") Long postId, @Param("numberOfLikes") int numberOfLikes);

    Long countByUser_Id(Long userId);
    @Query(value = """
    SELECT p.* FROM posts p
    LEFT JOIN users u ON p.user_id = u.id
    WHERE p.user_id <> :currentUserId
    ORDER BY 
        p.post_date DESC,
        (CASE WHEN u.id IN :followIds THEN 100 ELSE 0 END +
         CASE WHEN p.post_id IN :followLikes THEN 70 ELSE 0 END +
         (CASE WHEN p.number_of_likes > 50 THEN 30
               WHEN p.number_of_likes > 10 THEN 15
               ELSE 0 END) +
         CASE WHEN u.id NOT IN :followIds THEN 10 ELSE 0 END) DESC
    """, nativeQuery = true)
    Page<Post> findHomePagePostsNative(
            @Param("currentUserId") Long currentUserId,
            @Param("followIds") List<Long> followIds,
            @Param("followLikes") List<Long> followLikes,
            Pageable pageable
    );

    Page<Post> findByPostNameContainingIgnoreCase(String keyword, Pageable pageable);
}

