package com.beem.beem_sunucu.Users;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class User_Controller {
    private final User_service userService;

    public User_Controller(User_service userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String>register(@Valid @RequestBody User_Request_DTO userRequestDto){
        userService.Register(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Kayıt başarılı!");
    }
    @PostMapping("/login")
    public ResponseEntity<User_Response_DTO>login(@RequestBody Login_DTO loginDto){
        User_Response_DTO responseDTO  = userService.Login(loginDto.getPassword(),loginDto.getUsername());
        return ResponseEntity.ok(responseDTO);
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request){
        String email = request.get("email");
        userService.forgotPassword(email);
        return ResponseEntity.ok("Şifre sıfırlama linki email adresinize gönderildi.");
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO dto){
        userService.resetPassword(dto.getToken(), dto.getNewPassword());
        return ResponseEntity.ok("Şifreniz başarıyla değiştirildi.");
    }
}
