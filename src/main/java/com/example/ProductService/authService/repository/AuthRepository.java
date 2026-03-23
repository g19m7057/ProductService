package com.example.ProductService.authService.repository;

import com.example.ProductService.authService.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<Profile, Long> {
    public Profile findByEmail(String email);
}
