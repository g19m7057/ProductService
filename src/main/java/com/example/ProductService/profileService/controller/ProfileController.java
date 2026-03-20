package com.example.ProductService.profileService.controller;

import com.example.ProductService.profileService.model.Profile;
import com.example.ProductService.profileService.service.ProfileService;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;
    private final Logger logger;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.logger = LoggerFactory.getLogger(ProfileController.class);
        this.profileService = profileService;
    }

    @PostMapping("/register")
    public Profile createProfile(@RequestBody Profile profile) {
        logger.info("Profile created");
        return profileService.createProfile(profile);
    }

    @GetMapping
    public List<Profile> getProfiles() { return profileService.getProfiles(); }

}
