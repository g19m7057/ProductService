package com.example.ProductService.product.service;

import com.example.ProductService.product.model.Product;
import com.example.ProductService.product.model.ProductSummary;
import com.example.ProductService.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductSummary> getAllProducts() {
        return productRepository.findByIsActive(true);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found for id: " + id));
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setMonthlyFee(productDetails.getMonthlyFee());
        product.setCategory(productDetails.getCategory());
        product.setMinAmount(productDetails.getMinAmount());
        product.setMaxAmount(productDetails.getMaxAmount());
        product.setInterestRate(productDetails.getInterestRate());
        product.setTermMonths(productDetails.getTermMonths());
        product.setIsActive(productDetails.getIsActive());
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found for id: " + id));
        productRepository.delete(product);
    }
}
