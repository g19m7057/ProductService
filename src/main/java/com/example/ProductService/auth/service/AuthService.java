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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class AuthService implements ReactiveUserDetailsService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

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
                    Date dob;
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        dob = sdf.parse(request.getDob());
                    } catch (ParseException | NullPointerException e) {
                        return Mono.error(new IllegalArgumentException("Invalid date format. Expected dd/MM/yyyy", e));
                    }

                    Profile profile = Profile.builder()
                            .email(request.getEmail())
                            .name(request.getName())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .isSouthAfrican(request.isSouthAfrican())
                            .contactNumber(request.getContactNumber())
                            .identificationNumber(request.getIdentificationNumber())
                            .address(request.getAddress())
                            .customerType(request.getCustomerType())
                            .dob(dob)
                            .role("02")
                            .build();

                    System.out.println(profile);

                    return authRepository.save(profile);
                }))
                .map(profile -> {
                    String token = jwtService.generateToken(profile);
                    logger.info("Profile created:{}", profile.getEmail());
                    return AuthResponse.builder().token(token).build();
                });
    }

    public Flux<Profile> getProfiles() {
        return authRepository.findAll();
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
