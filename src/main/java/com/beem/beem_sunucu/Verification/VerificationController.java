package com.beem.beem_sunucu.Verification;

import com.beem.beem_sunucu.Users.User;
import com.beem.beem_sunucu.Users.User_Response_DTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class VerificationController {
    private final EmailService emailService;

    public VerificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/verify")
    public ResponseEntity<Map<String, String>> verify(@RequestParam String token) {
        String result = emailService.verifyEmail(token);

        Map<String, String> response = new HashMap<>();
        response.put("message", result);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<Map<String, String>> requestResetPassword(@RequestParam String email){
        return ResponseEntity.ok(emailService.forgotPassword(email));
    }

    @GetMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @RequestParam String token
    ) {
        return ResponseEntity.ok(emailService.verifyNewPassword(token));
    }

    @PostMapping("/savePassword")
    public ResponseEntity<Map<String, String>> saveNewPassword(
            @RequestParam String token,
            @RequestBody @Valid ResetPasswordDTO dto
    ) {
        return ResponseEntity.ok(emailService.saveNewPassword(token, dto));
    }

}
