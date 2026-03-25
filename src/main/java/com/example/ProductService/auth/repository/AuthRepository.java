package com.example.ProductService.auth.repository;

import com.example.ProductService.auth.model.Profile;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AuthRepository extends ReactiveCrudRepository<Profile, Long> {
    public Mono<Profile> findByEmail(String email);
}
