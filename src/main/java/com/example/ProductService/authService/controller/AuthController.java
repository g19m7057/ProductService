package com.example.ProductService.authService.controller;

import com.example.ProductService.authService.model.LoginRequest;
import com.example.ProductService.authService.model.AuthResponse;
import com.example.ProductService.authService.model.Profile;
import com.example.ProductService.authService.model.RegisterRequest;
import com.example.ProductService.authService.service.authService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final authService authService;
    @Autowired
    public AuthController(authService authService) {
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
