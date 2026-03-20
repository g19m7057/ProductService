package com.example.ProductService.profileService.service;

import com.example.ProductService.profileService.model.Profile;
import com.example.ProductService.profileService.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile createProfile(Profile profile) {
        // check if there's already a profile with the email
        // and hash the password
        return profileRepository.save(profile);
    }

    public List<Profile> getProfiles() {
        return profileRepository.findAll();
    }
}
