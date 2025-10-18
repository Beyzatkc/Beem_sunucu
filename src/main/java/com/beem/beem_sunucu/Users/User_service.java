package com.beem.beem_sunucu.Users;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class User_service {
    private final User_Repo userRepo;
    private final PasswordEncoder passwordEncoder;
   // private final EmailService emailService;
    //private final EmailService emailService;

    public User_service(User_Repo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        //this.emailService = emailService;
    }
    @Transactional
    public User_Response_DTO register(User_Request_DTO user){
        if(userRepo.existsByUsername(user.getUsername())){
            throw new CustomExceptions.UserAlreadyExistsException("Kullanıcı adı zaten alınmış.");
        }

        if(userRepo.existsByEmail(user.getEmail())){
            throw new CustomExceptions.UserAlreadyExistsException("Bu Email zaten kayıtlı");
        }
        User entity=new User();
        entity.setUsername(user.getUsername());
        entity.setPassword(passwordEncoder.encode(user.getPassword()));
        entity.setName(user.getName());
        entity.setSurname(user.getSurname());
        entity.setDate(LocalDateTime.now());
        entity.setBiography(user.getBiography());
        entity.setProfile(user.getProfile());
        entity.setEmail(user.getEmail());
        userRepo.saveAndFlush(entity);
        return new User_Response_DTO(entity);
    }
    public User_Response_DTO login(String password,String username){
        User user=userRepo
                .findByUsername(username)
                .orElseThrow(()->new CustomExceptions.AuthenticationException("Kullanıcı adı hatalı."));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomExceptions.AuthenticationException("Parola hatalı.");
        }
       return new User_Response_DTO(user);
    }
    /*
    public void forgotPassword(String email){
        User user = userRepo.findByEmail(email)
                .orElseThrow(()->new CustomExceptions.AuthenticationException("Email bulunamadı."));
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepo.save(user);
      //  emailService.sendResetEmail(user.getEmail(), token);
    }
    public void resetPassword(String token,String newPassword){
        if (newPassword.length() < 6) {
            throw new CustomExceptions.ValidationException("Şifre en az 6 karakter olmalı");
        }
        User user = userRepo.findByResetToken(token)
                .orElseThrow(() -> new CustomExceptions.AuthenticationException("Geçersiz veya süresi dolmuş"));

        if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new CustomExceptions.AuthenticationException("Süresi dolmuş");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setTokenExpiry(null);
        userRepo.save(user);
    }*/
}