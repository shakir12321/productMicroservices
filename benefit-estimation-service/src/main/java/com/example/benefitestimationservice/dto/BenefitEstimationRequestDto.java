package com.example.benefitestimationservice.dto;

import jakarta.validation.constraints.NotNull;
import com.example.benefitestimationservice.model.BenefitType;

public class BenefitEstimationRequestDto {
    
    @NotNull(message = "Order ID is required")
    private Long orderId;
    
    @NotNull(message = "Customer ID is required")
    private String customerId;
    
    private BenefitType preferredBenefitType;
    
    // Default constructor
    public BenefitEstimationRequestDto() {}
    
    // Constructor with fields
    public BenefitEstimationRequestDto(Long orderId, String customerId, BenefitType preferredBenefitType) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.preferredBenefitType = preferredBenefitType;
    }
    
    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }
    
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public BenefitType getPreferredBenefitType() {
        return preferredBenefitType;
    }
    
    public void setPreferredBenefitType(BenefitType preferredBenefitType) {
        this.preferredBenefitType = preferredBenefitType;
    }
    
    @Override
    public String toString() {
        return "BenefitEstimationRequestDto{" +
                "orderId=" + orderId +
                ", customerId='" + customerId + '\'' +
                ", preferredBenefitType=" + preferredBenefitType +
                '}';
    }
}
