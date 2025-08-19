package com.example.benefitestimationservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "benefit_estimations")
public class BenefitEstimation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Order ID is required")
    @Column(nullable = false)
    private Long orderId;
    
    @NotNull(message = "Customer ID is required")
    @Column(nullable = false)
    private String customerId;
    
    @NotNull(message = "Estimated benefit amount is required")
    @Positive(message = "Estimated benefit amount must be positive")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedBenefitAmount;
    
    @NotNull(message = "Order total amount is required")
    @Positive(message = "Order total amount must be positive")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal orderTotalAmount;
    
    @Column(nullable = false)
    private LocalDateTime estimationDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BenefitType benefitType;
    
    @Column(length = 500)
    private String calculationDetails;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstimationStatus status;
    
    // Default constructor
    public BenefitEstimation() {
        this.estimationDate = LocalDateTime.now();
        this.status = EstimationStatus.PENDING;
    }
    
    // Constructor with fields
    public BenefitEstimation(Long orderId, String customerId, BigDecimal estimatedBenefitAmount, 
                           BigDecimal orderTotalAmount, BenefitType benefitType) {
        this();
        this.orderId = orderId;
        this.customerId = customerId;
        this.estimatedBenefitAmount = estimatedBenefitAmount;
        this.orderTotalAmount = orderTotalAmount;
        this.benefitType = benefitType;
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
        return "BenefitEstimation{" +
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
