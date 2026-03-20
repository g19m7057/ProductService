package com.example.ProductService.profileService.repository;

import com.example.ProductService.profileService.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    public List<Profile> findByEmail(String email);
}
