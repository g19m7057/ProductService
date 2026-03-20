package com.example.ProductService.productService.repository;

import com.example.ProductService.productService.model.Product;
import com.example.ProductService.productService.model.ProductSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<ProductSummary> findByIsActive(boolean isActive);
}
