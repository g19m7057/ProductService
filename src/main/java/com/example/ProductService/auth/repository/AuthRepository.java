package com.example.ProductService.auth.repository;

import com.example.ProductService.auth.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Profile, Long> {
    public Optional<Profile> findByEmail(String email);
}
