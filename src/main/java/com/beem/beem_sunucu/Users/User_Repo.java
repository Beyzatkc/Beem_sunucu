package com.beem.beem_sunucu.Users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface User_Repo extends JpaRepository<User,Long> {
    Optional<User>findByUsername(String username);
    Optional<User>findByEmail(String email);
    Optional<User>findByResetToken(String token);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    Page<User> findAllByIdIn(List<Long> ids, Pageable pageable);
}
