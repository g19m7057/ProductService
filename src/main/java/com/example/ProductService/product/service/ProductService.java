package com.example.ProductService.product.service;

import com.example.ProductService.auth.model.Profile;
import com.example.ProductService.product.model.Product;
import com.example.ProductService.product.model.ProductSummary;
import com.example.ProductService.product.model.UserProducts;
import com.example.ProductService.product.repository.ProductRepository;
import com.example.ProductService.product.repository.UserProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.Period;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserProductsRepository userProductsRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, UserProductsRepository userProductsRepository) {
        this.productRepository = productRepository;
        this.userProductsRepository = userProductsRepository;
    }

    public Flux<ProductSummary> getAllProducts() {
        return productRepository.findByIsActive(true);
    }

    public Mono<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Mono<Product> takeProductById(Long productId) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(auth -> {
                    if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof Profile profile)) {
                        return Mono.error(new RuntimeException("User not authenticated"));
                    }
                    return userProductsRepository.existsByCustomerIdAndProductId(profile.getId(), productId)
                            .flatMap(exists -> {
                                if (exists) {
                                    return Mono.error(new RuntimeException("User already has this product"));
                                }
                                return productRepository.findById(productId)
                                        .switchIfEmpty(Mono.error(new RuntimeException("Product not found")))
                                        .flatMap(product -> {
                                            if (!Boolean.TRUE.equals(product.getIsActive())) {
                                                return Mono.error(new RuntimeException("Product is not active"));
                                            }
                                            if (!isEligible(profile, product)) {
                                                return Mono.error(new RuntimeException("User not eligible for product"));
                                            }
                                            UserProducts userProduct = UserProducts.builder()
                                                    .productId(product.getId())
                                                    .customerId(profile.getId())
                                                    .build();
                                            return userProductsRepository.save(userProduct)
                                                    .thenReturn(product);
                                        });
                            });
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Security context or authentication not found")));
    }

    private boolean isEligible(Profile profile, Product product) {
        if (product.getRequiredCountryCode() != null && !product.getRequiredCountryCode().trim().isEmpty() && !product.getRequiredCountryCode().equals(profile.getCountryCode())) {
            return false;
        }
        if (product.getRequiredMaritalStatus() != null 
                && !"false".equalsIgnoreCase(product.getRequiredMaritalStatus())
                && !product.getRequiredMaritalStatus().equals(profile.getMaritalStatus())) {
            return false;
        }
        if (product.getRequiredCustomerType() != null && product.getRequiredCustomerType() != profile.getCustomerType()) {
            return false;
        }
        if (product.getMinAge() != null) {
            int age = Period.between(profile.getDob(), LocalDate.now()).getYears();
            if (age < product.getMinAge()) {
                return false;
            }
        }
        return true;
    }
}
