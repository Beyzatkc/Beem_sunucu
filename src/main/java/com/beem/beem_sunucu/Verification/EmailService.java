package com.beem.beem_sunucu.Verification;

import com.beem.beem_sunucu.Users.User;
import com.beem.beem_sunucu.Users.User_Repo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final TokenRepo tokenRepo;
    private final User_Repo userRepo;

    @Value("${app.base-url}")
    private String baseURL;


    public EmailService(JavaMailSender mailSender, TokenRepo tokenRepo, User_Repo userRepo) {
        this.mailSender = mailSender;
        this.tokenRepo = tokenRepo;
        this.userRepo = userRepo;
    }
    public void sendVerificationMail(String toEmail, String token) {

        String subject = "Email Doğrulama";
        String verificationLink =
                baseURL + "/auth/verify?token=" + token;

        String body =
                "Merhaba,\n\n" +
                        "Hesabınızı doğrulamak için aşağıdaki linke tıklayın:\n" +
                        verificationLink +
                        "\n\nBu link 5 dakika geçerlidir.";

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("beemdevops@gmail.com");
        mail.setTo(toEmail);
        mail.setSubject(subject);
        mail.setText(body);
        mailSender.send(mail);
    }
    public String verifyEmail(String token){
        EmailVerificationToken verification =
                tokenRepo.findByToken(token)
                        .orElseThrow(() -> new RuntimeException("Token geçersiz"));

        if (verification.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "Token süresi dolmuş";
        }

        User user = verification.getUser();
        user.setEmailVerified(true);
        userRepo.save(user);
        tokenRepo.delete(verification);

        return "Email doğrulandı";
    }
}
