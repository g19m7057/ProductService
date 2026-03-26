package com.example.ProductService.product.repository;

import com.example.ProductService.product.model.UserProducts;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserProductsRepository extends ReactiveCrudRepository<UserProducts, Long> {
    Mono<Boolean> existsByCustomerIdAndProductId(Long customerId, Long productId);
}
