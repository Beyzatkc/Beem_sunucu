package com.beem.beem_sunucu.Users;
import com.beem.beem_sunucu.Verification.EmailService;
import com.beem.beem_sunucu.Verification.EmailVerificationToken;
import com.beem.beem_sunucu.Verification.TokenRepo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class User_service implements UserDetailsService {
    private final User_Repo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepo tokenRepo;
    private final EmailService emailService;

    public User_service(User_Repo userRepo, PasswordEncoder passwordEncoder, TokenRepo tokenRepo, EmailService emailService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepo = tokenRepo;
        this.emailService = emailService;
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
        entity.setEmailVerified(false);
        userRepo.saveAndFlush(entity);

        String token=UUID.randomUUID().toString();

        EmailVerificationToken verification=new EmailVerificationToken();
        verification.setToken(token);
        verification.setUser(entity);
        verification.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        tokenRepo.saveAndFlush(verification);
        emailService.sendVerificationMail(user.getEmail(), token);

        return new User_Response_DTO(entity);
    }
    public User_Response_DTO getUserDtoByUsername(String username){
        User user=userRepo
                .findByUsername(username)
                .orElseThrow(()->new CustomExceptions.AuthenticationException("Kullanıcı adı hatalı."));
        return new User_Response_DTO(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("Kullanıcı bulunamadı"));

        return new CustomUserDetails(user);
    }

    public Long getUserIdByUsername(String username) {
        return userRepo.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    public Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();
    }

    public void securityUser(Long id){
        if(!getCurrentUserId().equals(id))
            throw new SecurityException("Yasaklı erişim.");
    }
    public void securityUser(String  name){
        if(!SecurityContextHolder.getContext().getAuthentication().getName().equals(name))
            throw new SecurityException("Yasaklı erişim.");
    }

}