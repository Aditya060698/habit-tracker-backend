package com.aditya.habittracker.controllers;

import com.aditya.habittracker.config.JwtUtil;
import com.aditya.habittracker.entity.User;
import com.aditya.habittracker.repositories.UserRepository;
// import com.tb.travelbuddy.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Handles user sign-up.
     */
    @PostMapping("/signup")
    public String signUp(@RequestBody User userRequest) {
        User user = new User();
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        userRepository.save(user);
        return "User registered successfully!";
    }

    /**
     * Handles user login.
     */
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

      
        String token = jwtUtil.generateToken(user);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }
}
