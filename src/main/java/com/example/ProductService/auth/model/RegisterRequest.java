package com.example.ProductService.auth.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

@Data
public class RegisterRequest {
    @NonNull @Email
    private String email;

    @NonNull
    private String name;

    @NonNull @Size(min = 8)
    private String password;

    @NonNull @Size(max = 2)
    private String countryCode;

    @NonNull
    private String contactNumber;

    @NonNull
    private String identificationNumber;

    @NonNull
    private String address;

    private String maritalStatus;

    private int customerType;

    @NonNull
    private String dob;
}
