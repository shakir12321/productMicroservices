package com.example.payoutservice.dto;

import jakarta.validation.constraints.NotNull;
import com.example.payoutservice.model.PayoutMethod;

public class PayoutRequestDto {
    
    @NotNull(message = "Benefit estimation ID is required")
    private Long benefitEstimationId;
    
    @NotNull(message = "Payout method is required")
    private PayoutMethod payoutMethod;
    
    private String additionalDetails;
    
    // Default constructor
    public PayoutRequestDto() {}
    
    // Constructor with fields
    public PayoutRequestDto(Long benefitEstimationId, PayoutMethod payoutMethod, String additionalDetails) {
        this.benefitEstimationId = benefitEstimationId;
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
                ", payoutMethod=" + payoutMethod +
                ", additionalDetails='" + additionalDetails + '\'' +
                '}';
    }
}
