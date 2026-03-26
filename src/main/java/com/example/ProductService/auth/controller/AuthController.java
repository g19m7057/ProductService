package com.example.ProductService.auth.controller;

import com.example.ProductService.auth.model.LoginRequest;
import com.example.ProductService.auth.model.AuthResponse;
import com.example.ProductService.auth.model.RegisterRequest;
import com.example.ProductService.auth.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

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
    public Mono<ResponseEntity<AuthResponse>> createProfile(@RequestBody RegisterRequest request) {
        logger.info("Creating Profile");
        return authService.createProfile(request)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        logger.info("Login Request");
        return authService.login(request)
                .map(ResponseEntity::ok);
    }
}
