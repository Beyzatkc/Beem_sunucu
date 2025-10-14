
/*
package com.beem.beem_sunucu.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetEmail(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Şifre Sıfırlama Talebi");
        message.setText("Şifrenizi sıfırlamak için linke tıklayın: " +
                "http://localhost:3000/reset-password?token=" + token);
        mailSender.send(message);
    }
}
*/