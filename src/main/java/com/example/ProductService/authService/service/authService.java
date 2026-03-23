package com.example.ProductService.authService.service;

import com.example.ProductService.authHelpers.Jwt.JwtService;
import com.example.ProductService.authService.model.LoginRequest;
import com.example.ProductService.authService.model.AuthResponse;
import com.example.ProductService.authService.model.Profile;
import com.example.ProductService.authService.model.RegisterRequest;
import com.example.ProductService.authService.repository.AuthRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class authService implements UserDetailsService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final Logger logger = LoggerFactory.getLogger(authService.class);

    @Autowired
    public authService(AuthRepository authRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse createProfile(RegisterRequest request) {
        if(authRepository.findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("Invalid email or password");
        }

        Profile profile = Profile.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("02")
                .build();

        authRepository.save(profile);

        String token = jwtService.generateToken(profile);
        logger.info("Profile created:{}", profile.getEmail());

        return AuthResponse.builder().token(token).build();
    }

    public List<Profile> getProfiles() {
        return authRepository.findAll();
    }

    public AuthResponse login(LoginRequest request) {
        Profile profile = authRepository.findByEmail(request.getEmail());

        if(profile == null) {
            throw new RuntimeException("Invalid email or password");
        }

        if(!passwordEncoder.matches(request.getPassword(), profile.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(profile);
        return AuthResponse.builder().token(token).build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authRepository.findByEmail(username);
//                .orElseThrow(() => new UsernameNotFoundException("User not found"));
    }
}
