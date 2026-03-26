package com.example.ProductService.product.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@Table(name = "user_products", schema = "product")
@AllArgsConstructor
@NoArgsConstructor
public class UserProducts {
    @Id
    private Long id;

    @Column("product_id")
    private Long productId;

    @Column("customer_id")
    private Long customerId;
}