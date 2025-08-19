package com.example.orderservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class OrderRequestDto {
    
    @NotBlank(message = "Customer name is required")
    private String customerName;
    
    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private String customerEmail;
    
    private String shippingAddress;
    
    @NotEmpty(message = "Order items cannot be empty")
    @Valid
    private List<OrderItemRequestDto> orderItems;
    
    // Default constructor
    public OrderRequestDto() {}
    
    // Constructor with fields
    public OrderRequestDto(String customerName, String customerEmail, String shippingAddress, List<OrderItemRequestDto> orderItems) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.shippingAddress = shippingAddress;
        this.orderItems = orderItems;
    }
    
    // Getters and Setters
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerEmail() {
        return customerEmail;
    }
    
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    
    public String getShippingAddress() {
        return shippingAddress;
    }
    
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    
    public List<OrderItemRequestDto> getOrderItems() {
        return orderItems;
    }
    
    public void setOrderItems(List<OrderItemRequestDto> orderItems) {
        this.orderItems = orderItems;
    }
    
    @Override
    public String toString() {
        return "OrderRequestDto{" +
                "customerName='" + customerName + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", orderItems=" + orderItems +
                '}';
    }
}
