package com.example.ProductService.product.controller;

import com.example.ProductService.product.model.Product;
import com.example.ProductService.product.model.ProductSummary;
import com.example.ProductService.product.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    private static final Logger logger = LogManager.getLogger(ProductController.class);

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Flux<ProductSummary> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Product>> getProductById(@PathVariable Long id) {
        logger.info("Get Product by ID");
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/take")
    public Mono<ResponseEntity<Object>> takeProductById(@PathVariable Long id) {
        logger.info("Take product by product ID");
        return productService.takeProductById(id)
                .map(product -> ResponseEntity.ok((Object) product))
                .onErrorResume(e -> {
                    logger.error("Error taking product: " + e.getMessage());
                    if (e.getMessage().contains("not found")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()));
                    }
                    if (e.getMessage().contains("already has this product")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()));
                    }
                    if (e.getMessage().contains("not eligible") || e.getMessage().contains("not active")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage()));
                    }
                    if (e.getMessage().contains("not authenticated") || e.getMessage().contains("not found")) {
                        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage()));
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()));
                });
    }
}
