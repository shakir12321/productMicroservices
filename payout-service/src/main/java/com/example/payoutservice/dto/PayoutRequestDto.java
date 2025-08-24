package com.example.payoutservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import com.example.payoutservice.model.PayoutMethod;
import java.math.BigDecimal;

public class PayoutRequestDto {
    
    @NotNull(message = "Benefit estimation ID is required")
    private Long benefitEstimationId;
    
    @NotNull(message = "Customer ID is required")
    private String customerId;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
    
    @NotNull(message = "Payout method is required")
    private PayoutMethod payoutMethod;
    
    private String additionalDetails;
    
    // Default constructor
    public PayoutRequestDto() {}
    
    // Constructor with fields
    public PayoutRequestDto(Long benefitEstimationId, String customerId, BigDecimal amount, PayoutMethod payoutMethod, String additionalDetails) {
        this.benefitEstimationId = benefitEstimationId;
        this.customerId = customerId;
        this.amount = amount;
        this.payoutMethod = payoutMethod;
        this.additionalDetails = additionalDetails;
    }
    
    // Getters and Setters
    public Long getBenefitEstimationId() {
        return benefitEstimationId;
    }
    
    public void setBenefitEstimationId(Long benefitEstimationId) {
        this.benefitEstimationId = benefitEstimationId;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public PayoutMethod getPayoutMethod() {
        return payoutMethod;
    }
    
    public void setPayoutMethod(PayoutMethod payoutMethod) {
        this.payoutMethod = payoutMethod;
    }
    
    public String getAdditionalDetails() {
        return additionalDetails;
    }
    
    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
    }
    
    @Override
    public String toString() {
        return "PayoutRequestDto{" +
                "benefitEstimationId=" + benefitEstimationId +
                ", customerId='" + customerId + '\'' +
                ", amount=" + amount +
                ", payoutMethod=" + payoutMethod +
                ", additionalDetails='" + additionalDetails + '\'' +
                '}';
    }
}
