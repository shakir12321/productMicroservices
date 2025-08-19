package com.example.payoutservice.dto;

import com.example.payoutservice.model.BenefitType;
import com.example.payoutservice.model.EstimationStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BenefitEstimationDto {
    private Long id;
    private Long orderId;
    private String customerId;
    private BigDecimal estimatedBenefitAmount;
    private BigDecimal orderTotalAmount;
    private LocalDateTime estimationDate;
    private BenefitType benefitType;
    private String calculationDetails;
    private EstimationStatus status;
    
    // Default constructor
    public BenefitEstimationDto() {}
    
    // Constructor with fields
    public BenefitEstimationDto(Long id, Long orderId, String customerId, BigDecimal estimatedBenefitAmount,
                               BigDecimal orderTotalAmount, LocalDateTime estimationDate, BenefitType benefitType,
                               String calculationDetails, EstimationStatus status) {
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.estimatedBenefitAmount = estimatedBenefitAmount;
        this.orderTotalAmount = orderTotalAmount;
        this.estimationDate = estimationDate;
        this.benefitType = benefitType;
        this.calculationDetails = calculationDetails;
        this.status = status;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public BigDecimal getEstimatedBenefitAmount() {
        return estimatedBenefitAmount;
    }
    
    public void setEstimatedBenefitAmount(BigDecimal estimatedBenefitAmount) {
        this.estimatedBenefitAmount = estimatedBenefitAmount;
    }
    
    public BigDecimal getOrderTotalAmount() {
        return orderTotalAmount;
    }
    
    public void setOrderTotalAmount(BigDecimal orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }
    
    public LocalDateTime getEstimationDate() {
        return estimationDate;
    }
    
    public void setEstimationDate(LocalDateTime estimationDate) {
        this.estimationDate = estimationDate;
    }
    
    public BenefitType getBenefitType() {
        return benefitType;
    }
    
    public void setBenefitType(BenefitType benefitType) {
        this.benefitType = benefitType;
    }
    
    public String getCalculationDetails() {
        return calculationDetails;
    }
    
    public void setCalculationDetails(String calculationDetails) {
        this.calculationDetails = calculationDetails;
    }
    
    public EstimationStatus getStatus() {
        return status;
    }
    
    public void setStatus(EstimationStatus status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "BenefitEstimationDto{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", customerId='" + customerId + '\'' +
                ", estimatedBenefitAmount=" + estimatedBenefitAmount +
                ", orderTotalAmount=" + orderTotalAmount +
                ", estimationDate=" + estimationDate +
                ", benefitType=" + benefitType +
                ", calculationDetails='" + calculationDetails + '\'' +
                ", status=" + status +
                '}';
    }
}
