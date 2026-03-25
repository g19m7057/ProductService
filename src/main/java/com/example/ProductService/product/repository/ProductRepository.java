package com.example.ProductService.product.repository;

import com.example.ProductService.product.model.Product;
import com.example.ProductService.product.model.ProductSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<ProductSummary> findByIsActive(boolean isActive);
}
