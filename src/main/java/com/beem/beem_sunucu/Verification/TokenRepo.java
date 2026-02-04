package com.beem.beem_sunucu.Verification;

import com.beem.beem_sunucu.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepo extends JpaRepository<EmailVerificationToken,Long> {
    Optional<EmailVerificationToken>findByToken(String token);
    Optional<EmailVerificationToken>findByUser_Id(Long id);
}
