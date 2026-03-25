package com.example.ProductService.auth.model;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String name;
    private String password;
    private String citizenship;
    private String contactNumber;
    private String identificationNumber;
    private String address;
    private String customerType;
    private String dob;
}
