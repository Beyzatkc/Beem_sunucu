package com.beem.beem_sunucu.Verification;

import com.beem.beem_sunucu.Users.CustomExceptions;
import com.beem.beem_sunucu.Users.User;
import com.beem.beem_sunucu.Users.User_Repo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepo tokenRepo;
    private final User_Repo userRepo;

    @Value("${app.base-url}")
    private String baseURL;


    public EmailService(JavaMailSender mailSender, PasswordEncoder passwordEncoder, TokenRepo tokenRepo, User_Repo userRepo) {
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
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
                        .orElseThrow(() -> new CustomExceptions.InvalidTokenException("Token geçersiz"));

        if (verification.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw  new CustomExceptions.InvalidTokenException("Token süresi dolmuş");
        }

        User user = verification.getUser();
        user.setEmailVerified(true);
        userRepo.save(user);
        tokenRepo.delete(verification);

        return "Email doğrulandı";
    }
    public  Map<String, String> forgotPassword(String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new CustomExceptions.NotFoundException("Kullanıcı bulunamadı!"));
        if(!user.isEmailVerified()){
            throw new CustomExceptions.ValidationException("Email doğrulaması yapılmamış");
        }

        Optional<EmailVerificationToken> existingToken = tokenRepo.findByUser_Id(user.getId());

        existingToken.ifPresent(token -> {
            if (token.getExpiryDate().isAfter(LocalDateTime.now())) {
                throw new CustomExceptions.DuplicateTokenException(
                        "Kullanıcı için aktif bir şifre reset linki zaten mevcut. Lütfen mailinizi kontrol edin."
                );
            } else {
                tokenRepo.delete(token);
            }
        });

        String token = UUID.randomUUID().toString();

        EmailVerificationToken resetToken = new EmailVerificationToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(
                LocalDateTime.now().plusMinutes(5)
        );

        tokenRepo.save(resetToken);

        forgotPasswordMail(email, token);
        return Map.of("message", "Basarili");
    }
    public void forgotPasswordMail(String toEmail, String token) {

        String subject = "Şifre Sıfırlama Talebi";

        String resetLink =
                baseURL + "/auth/reset-password?token=" + token;

        String body =
                "Merhaba,\n\n" +
                        "Şifrenizi sıfırlamak için aşağıdaki bağlantıya tıklayın:\n\n" +
                        resetLink +
                        "\n\nBu bağlantı 5 dakika boyunca geçerlidir.\n" +
                        "Eğer bu isteği siz yapmadıysanız, lütfen bu e-postayı dikkate almayın.";

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("beemdevops@gmail.com");
        mail.setTo(toEmail);
        mail.setSubject(subject);
        mail.setText(body);

        mailSender.send(mail);
    }
    public Map<String, String> verifyNewPassword(String token){
        EmailVerificationToken verification =
                tokenRepo.findByToken(token)
                        .orElseThrow(() -> new CustomExceptions.InvalidTokenException("Token geçersiz"));

        if (verification.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new CustomExceptions.InvalidTokenException("Token süresi dolmuş");
        }

        return Map.of("message", "Token geçerli");
    }

    public Map<String, String> saveNewPassword(String token, ResetPasswordDTO dto){

        EmailVerificationToken verification =
                tokenRepo.findByToken(token)
                        .orElseThrow(() -> new SecurityException("Token geçersiz"));

        if (verification.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new CustomExceptions.InvalidTokenException("Token süresi dolmuş");
        }
        if(!dto.getNewPassword().equals(dto.getConfirmPassword())){
            throw new CustomExceptions.AuthorizationException("Şifreler uyuşmuyor");
        }

        User user = verification.getUser();
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));

        userRepo.save(user);
        tokenRepo.delete(verification);

        return Map.of("message", "Şifre değiştirildi.Giriş yapabilirsiniz!");
    }


}
