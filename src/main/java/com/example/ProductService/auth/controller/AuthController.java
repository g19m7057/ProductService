package com.example.ProductService.auth.controller;

import com.example.ProductService.auth.model.LoginRequest;
import com.example.ProductService.auth.model.AuthResponse;
import com.example.ProductService.auth.model.Profile;
import com.example.ProductService.auth.model.RegisterRequest;
import com.example.ProductService.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public  ResponseEntity<AuthResponse>  createProfile(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.createProfile(request));
    }

    @GetMapping
    public ResponseEntity<List<Profile>> getProfiles() { return ResponseEntity.ok(authService.getProfiles()); }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
