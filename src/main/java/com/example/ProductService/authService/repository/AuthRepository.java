package com.example.ProductService.authService.repository;

import com.example.ProductService.authService.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Profile, Long> {
    public Optional<Profile> findByEmail(String email);
}
