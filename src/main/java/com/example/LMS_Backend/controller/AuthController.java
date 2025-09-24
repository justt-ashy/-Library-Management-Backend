package com.example.LMS_Backend.controller;

import com.example.LMS_Backend.model.User;
import com.example.LMS_Backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user) {
        User created = userService.signup(user);
        return ResponseEntity.ok(created);
    }

    public static class LoginRequest {
        public String username;
        public String password;
    }

    public static class LoginResponse {
        public String message;
        public String username;
        public String role;
        public LoginResponse(String message, String username, String role) {
            this.message = message;
            this.username = username;
            this.role = role;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return userService.getUserByUsername(request.username)
                .filter(u -> passwordEncoder.matches(request.password, u.getPassword()))
                .map(u -> ResponseEntity.ok(new LoginResponse("LOGIN_SUCCESS", u.getUsername(), u.getRole())))
                .orElse(ResponseEntity.status(401).build());
    }
}


