package com.beem.beem_sunucu.Posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Post_Repo extends JpaRepository<Post,Long> {
    Page<Post> findByKisiId(Long userId, Pageable pageable);
}

