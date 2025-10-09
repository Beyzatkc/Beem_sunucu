package com.beem.beem_sunucu.User;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @GetMapping("/allusers")
    public List<User> allUsers(){
        return userRepository.findAll();
    }

    @PostMapping("/register")
    public User createUser(@RequestBody User user){
        return userRepository.save(user);
    }

    @GetMapping("/user{id}")
    public User getUserByID(Long id){
        return userRepository.findById(id).orElse(null);
    }
}
