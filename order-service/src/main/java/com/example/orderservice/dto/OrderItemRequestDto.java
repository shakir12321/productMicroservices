package com.example.orderservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderItemRequestDto {
    
    @NotNull(message = "Product ID is required")
    private Long productId;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
    
    // Default constructor
    public OrderItemRequestDto() {}
    
    // Constructor with fields
    public OrderItemRequestDto(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
    
    // Getters and Setters
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    @Override
    public String toString() {
        return "OrderItemRequestDto{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
