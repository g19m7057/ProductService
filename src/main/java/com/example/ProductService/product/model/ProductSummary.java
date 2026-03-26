package com.example.ProductService.product.model;

public interface ProductSummary {
    String getName();
    String getDescription();
    Double getMonthlyFee();
    String getRequiredCountryCode();
    String getRequiredMaritalStatus();
    Integer getRequiredCustomerType();
    Integer getMinAge();
}
