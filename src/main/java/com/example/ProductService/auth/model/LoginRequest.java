package com.example.ProductService.auth.model;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class LoginRequest {
    @NonNull @Email
    private String email;

    @NonNull
    private String password;
}

