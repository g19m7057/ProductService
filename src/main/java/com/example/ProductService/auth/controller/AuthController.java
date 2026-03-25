package com.example.ProductService.auth.controller;

import com.example.ProductService.auth.model.LoginRequest;
import com.example.ProductService.auth.model.AuthResponse;
import com.example.ProductService.auth.model.Profile;
import com.example.ProductService.auth.model.RegisterRequest;
import com.example.ProductService.auth.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public  ResponseEntity<AuthResponse>  createProfile(@RequestBody RegisterRequest request) {
        logger.info("Creating Profile");
        return ResponseEntity.ok(authService.createProfile(request));
    }

    @GetMapping
    public ResponseEntity<List<Profile>> getProfiles() {
        logger.info("Getting Profiles");
        return ResponseEntity.ok(authService.getProfiles());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        logger.info("Login Request");
        return ResponseEntity.ok(authService.login(request));
    }
}
