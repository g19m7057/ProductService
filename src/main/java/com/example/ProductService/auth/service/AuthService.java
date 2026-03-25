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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AuthService implements UserDetailsService {

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

    public AuthResponse createProfile(RegisterRequest request) {
        if(authRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        
        Date dob;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            dob = sdf.parse(request.getDob());
        } catch (ParseException | NullPointerException e) {
            throw new IllegalArgumentException("Invalid date format. Expected dd/MM/yyyy", e);
        }

        Profile profile = Profile.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .citizenship(request.getCitizenship())
                .contactNumber(request.getContactNumber())
                .identificationNumber(request.getIdentificationNumber())
                .address(request.getAddress())
                .customerType(request.getCustomerType())
                .dob(dob)
                .role("02")
                .build();

        System.out.println(profile);

        authRepository.save(profile);

        String token = jwtService.generateToken(profile);
        logger.info("Profile created:{}", profile.getEmail());

        return AuthResponse.builder().token(token).build();
    }

    public List<Profile> getProfiles() {
        return authRepository.findAll();
    }

    public AuthResponse login(LoginRequest request) {
        Profile profile = authRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid login details"));

        if(!passwordEncoder.matches(request.getPassword(), profile.getPassword())) {
            throw new RuntimeException("Invalid login details");
        }

        String token = jwtService.generateToken(profile);
        logger.info("Login successful, token created");

        return AuthResponse.builder().token(token).build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
