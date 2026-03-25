package com.example.ProductService.product.repository;

import com.example.ProductService.product.model.Product;
import com.example.ProductService.product.model.ProductSummary;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {

    Flux<ProductSummary> findByIsActive(boolean isActive);
}
