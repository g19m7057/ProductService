package com.example.ProductService.auth.service;

import com.example.ProductService.security.JwtService;
import com.example.ProductService.auth.model.LoginRequest;
import com.example.ProductService.auth.model.AuthResponse;
import com.example.ProductService.auth.model.Profile;
import com.example.ProductService.auth.model.RegisterRequest;
import com.example.ProductService.auth.repository.AuthRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class AuthService implements ReactiveUserDetailsService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Autowired
    public AuthService(AuthRepository authRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Mono<AuthResponse> createProfile(RegisterRequest request) {
        return authRepository.findByEmail(request.getEmail())
                .flatMap(existingProfile -> Mono.<Profile>error(new IllegalArgumentException("Invalid email or password")))
                .switchIfEmpty(Mono.defer(() -> {
                    LocalDate dob;
                    try {
                        dob = LocalDate.parse(request.getDob(), formatter);
                    } catch (DateTimeParseException | NullPointerException e) {
                        return Mono.error(new IllegalArgumentException("Invalid date format. Expected dd/MM/yyyy", e));
                    }

                    Profile profile = Profile.builder()
                            .email(request.getEmail())
                            .name(request.getName())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .countryCode(request.getCountryCode())
                            .contactNumber(request.getContactNumber())
                            .identificationNumber(request.getIdentificationNumber())
                            .address(request.getAddress())
                            .maritalStatus(request.getMaritalStatus())
                            .customerType(request.getCustomerType())
                            .dob(dob)
                            .role("02")
                            .build();

                    return authRepository.save(profile);
                }))
                .map(profile -> {
                    String token = jwtService.generateToken(profile);
                    logger.info("Profile created:{}", profile.getEmail());
                    return AuthResponse.builder().token(token).build();
                })
                .doOnError(error -> {
                    if (error instanceof IllegalArgumentException) {
                        logger.error("Validation error during profile creation: {}", error.getMessage());
                    } else {
                        logger.error("Repository failure during profile creation: {}", error.getMessage());
                    }
                }).onErrorMap(error -> {
                    if (error instanceof IllegalArgumentException) {
                        return error;
                    }
                    return new RuntimeException("Failed to create profile", error);
                });
    }

    public Mono<AuthResponse> login(LoginRequest request) {
        return authRepository.findByEmail(request.getEmail())
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Invalid login details")))
                .flatMap(profile -> {
                    if (!passwordEncoder.matches(request.getPassword(), profile.getPassword())) {
                        return Mono.error(new BadCredentialsException("Invalid login details"));
                    }

                    String token = jwtService.generateToken(profile);
                    logger.info("Login successful, token created");

                    return Mono.just(AuthResponse.builder().token(token).build());
                });
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return authRepository.findByEmail(username)
                .cast(UserDetails.class);
    }
}
