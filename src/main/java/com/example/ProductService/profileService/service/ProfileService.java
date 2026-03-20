package com.example.ProductService.profileService.service;

import com.example.ProductService.profileService.model.Profile;
import com.example.ProductService.profileService.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, PasswordEncoder passwordEncoder) {
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Profile createProfile(Profile profile) {
        if(!profileRepository.findByEmail(profile.getEmail()).isEmpty()) {
            throw new RuntimeException("Username already exists");
        }

        profile.setPassword(passwordEncoder.encode(profile.getPassword()));

        return profileRepository.save(profile);
    }

    public List<Profile> getProfiles() {
        return profileRepository.findAll();
    }
}
