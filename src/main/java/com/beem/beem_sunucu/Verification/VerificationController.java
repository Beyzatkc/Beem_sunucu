package com.beem.beem_sunucu.Verification;

import com.beem.beem_sunucu.Users.User;
import com.beem.beem_sunucu.Users.User_Response_DTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class VerificationController {
    private final EmailService emailService;

    public VerificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        String message = emailService.verifyEmail(token);
        return ResponseEntity.ok(message);
    }


}
