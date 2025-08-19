package com.example.payoutservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payouts")
public class Payout {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Benefit estimation ID is required")
    @Column(nullable = false)
    private Long benefitEstimationId;
    
    @NotNull(message = "Customer ID is required")
    @Column(nullable = false)
    private String customerId;
    
    @NotNull(message = "Order ID is required")
    @Column(nullable = false)
    private Long orderId;
    
    @NotNull(message = "Payout amount is required")
    @Positive(message = "Payout amount must be positive")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal payoutAmount;
    
    @Column(nullable = false)
    private LocalDateTime payoutDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayoutMethod payoutMethod;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayoutStatus status;
    
    @Column(length = 500)
    private String transactionDetails;
    
    @Column(length = 100)
    private String referenceNumber;
    
    @Column(length = 500)
    private String failureReason;
    
    // Default constructor
    public Payout() {
        this.payoutDate = LocalDateTime.now();
        this.status = PayoutStatus.PENDING;
    }
    
    // Constructor with fields
    public Payout(Long benefitEstimationId, String customerId, Long orderId, 
                 BigDecimal payoutAmount, PayoutMethod payoutMethod) {
        this();
        this.benefitEstimationId = benefitEstimationId;
        this.customerId = customerId;
        this.orderId = orderId;
        this.payoutAmount = payoutAmount;
        this.payoutMethod = payoutMethod;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public Long getOrderId() {
        return orderId;
    }
    
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    
    public BigDecimal getPayoutAmount() {
        return payoutAmount;
    }
    
    public void setPayoutAmount(BigDecimal payoutAmount) {
        this.payoutAmount = payoutAmount;
    }
    
    public LocalDateTime getPayoutDate() {
        return payoutDate;
    }
    
    public void setPayoutDate(LocalDateTime payoutDate) {
        this.payoutDate = payoutDate;
    }
    
    public PayoutMethod getPayoutMethod() {
        return payoutMethod;
    }
    
    public void setPayoutMethod(PayoutMethod payoutMethod) {
        this.payoutMethod = payoutMethod;
    }
    
    public PayoutStatus getStatus() {
        return status;
    }
    
    public void setStatus(PayoutStatus status) {
        this.status = status;
    }
    
    public String getTransactionDetails() {
        return transactionDetails;
    }
    
    public void setTransactionDetails(String transactionDetails) {
        this.transactionDetails = transactionDetails;
    }
    
    public String getReferenceNumber() {
        return referenceNumber;
    }
    
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
    
    public String getFailureReason() {
        return failureReason;
    }
    
    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
    
    @Override
    public String toString() {
        return "Payout{" +
                "id=" + id +
                ", benefitEstimationId=" + benefitEstimationId +
                ", customerId='" + customerId + '\'' +
                ", orderId=" + orderId +
                ", payoutAmount=" + payoutAmount +
                ", payoutDate=" + payoutDate +
                ", payoutMethod=" + payoutMethod +
                ", status=" + status +
                ", transactionDetails='" + transactionDetails + '\'' +
                ", referenceNumber='" + referenceNumber + '\'' +
                ", failureReason='" + failureReason + '\'' +
                '}';
    }
}
