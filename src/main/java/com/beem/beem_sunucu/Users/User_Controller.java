package com.beem.beem_sunucu.Users;

import com.beem.beem_sunucu.ServletFilter.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class User_Controller {
    private final User_service userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public User_Controller(User_service userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<User_Response_DTO> register(@Valid @RequestBody User_Request_DTO userRequestDto){
        User_Response_DTO dto = userService.register(userRequestDto);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>>login(@RequestBody Login_DTO loginDto){
        Authentication auth=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword()));
       User_Response_DTO responseDTO  = userService.getUserDtoByUsername(loginDto.getUsername());
        UserDetails userDetails=(UserDetails) auth.getPrincipal();
        String token= jwtUtil.generateToken(userDetails);
        Map<String, Object> map = new HashMap<>();
        map.put("user", responseDTO);
        map.put("token", token);
        return ResponseEntity.ok(map);
    }
    /*
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
     */
}
