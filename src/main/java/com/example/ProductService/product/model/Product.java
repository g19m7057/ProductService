package com.example.ProductService.product.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Builder
@Table(name = "products", schema = "product")
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    private Long id;

    private String name;

    private String description;

    @Column("monthly_fee")
    private BigDecimal monthlyFee;

    private String category;

    @Column("min_amount")
    private BigDecimal minAmount;

    @Column("max_amount")
    private BigDecimal maxAmount;

    @Column("interest_rate")
    private BigDecimal interestRate;

    @Column("term_months")
    private Integer termMonths;

    @Column("is_active")
    private Boolean isActive = true;

    @Column("created_at")
    private java.time.LocalDateTime createdAt;
}
