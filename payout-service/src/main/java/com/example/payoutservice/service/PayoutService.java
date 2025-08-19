package com.example.payoutservice.service;

import com.example.payoutservice.client.BenefitEstimationServiceClient;
import com.example.payoutservice.dto.PayoutRequestDto;
import com.example.payoutservice.dto.BenefitEstimationDto;
import com.example.payoutservice.model.Payout;
import com.example.payoutservice.model.PayoutMethod;
import com.example.payoutservice.model.PayoutStatus;
import com.example.payoutservice.repository.PayoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PayoutService {
    
    private final PayoutRepository payoutRepository;
    private final BenefitEstimationServiceClient benefitEstimationServiceClient;
    
    @Autowired
    public PayoutService(PayoutRepository payoutRepository, 
                        BenefitEstimationServiceClient benefitEstimationServiceClient) {
        this.payoutRepository = payoutRepository;
        this.benefitEstimationServiceClient = benefitEstimationServiceClient;
    }
    
    public List<Payout> getAllPayouts() {
        return payoutRepository.findAll();
    }
    
    public Optional<Payout> getPayoutById(Long id) {
        return payoutRepository.findById(id);
    }
    
    @Transactional
    public Payout createPayout(PayoutRequestDto request) {
        // Get benefit estimation details from benefit estimation service
        BenefitEstimationDto benefitEstimation = benefitEstimationServiceClient
                .getBenefitEstimationById(request.getBenefitEstimationId()).block();
        
        if (benefitEstimation == null) {
            throw new RuntimeException("Benefit estimation not found with ID: " + request.getBenefitEstimationId());
        }
        
        if (benefitEstimation.getStatus().name().equals("REJECTED") || 
            benefitEstimation.getStatus().name().equals("EXPIRED")) {
            throw new RuntimeException("Benefit estimation is not eligible for payout");
        }
        
        // Create payout
        Payout payout = new Payout();
        payout.setBenefitEstimationId(request.getBenefitEstimationId());
        payout.setCustomerId(benefitEstimation.getCustomerId());
        payout.setOrderId(benefitEstimation.getOrderId());
        payout.setPayoutAmount(benefitEstimation.getEstimatedBenefitAmount());
        payout.setPayoutMethod(request.getPayoutMethod());
        payout.setStatus(PayoutStatus.PENDING);
        payout.setReferenceNumber(generateReferenceNumber());
        payout.setTransactionDetails(generateTransactionDetails(benefitEstimation, request));
        
        return payoutRepository.save(payout);
    }
    
    private String generateReferenceNumber() {
        return "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private String generateTransactionDetails(BenefitEstimationDto benefitEstimation, PayoutRequestDto request) {
        return String.format("Payout for Order #%s, Benefit Type: %s, Amount: $%s, Method: %s, Details: %s",
                           benefitEstimation.getOrderId(), benefitEstimation.getBenefitType(),
                           benefitEstimation.getEstimatedBenefitAmount(), request.getPayoutMethod(),
                           request.getAdditionalDetails() != null ? request.getAdditionalDetails() : "N/A");
    }
    
    public Optional<Payout> processPayout(Long id) {
        return payoutRepository.findById(id)
                .map(payout -> {
                    try {
                        // Simulate payout processing
                        payout.setStatus(PayoutStatus.PROCESSING);
                        payout = payoutRepository.save(payout);
                        
                        // Simulate successful processing
                        payout.setStatus(PayoutStatus.COMPLETED);
                        payout.setTransactionDetails(payout.getTransactionDetails() + " - Processed successfully");
                        
                        return payoutRepository.save(payout);
                    } catch (Exception e) {
                        payout.setStatus(PayoutStatus.FAILED);
                        payout.setFailureReason("Processing failed: " + e.getMessage());
                        return payoutRepository.save(payout);
                    }
                });
    }
    
    public Optional<Payout> updatePayoutStatus(Long id, PayoutStatus status) {
        return payoutRepository.findById(id)
                .map(payout -> {
                    payout.setStatus(status);
                    return payoutRepository.save(payout);
                });
    }
    
    public boolean deletePayout(Long id) {
        if (payoutRepository.existsById(id)) {
            payoutRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<Payout> getPayoutsByCustomerId(String customerId) {
        return payoutRepository.findByCustomerId(customerId);
    }
    
    public List<Payout> getPayoutsByOrderId(Long orderId) {
        return payoutRepository.findByOrderId(orderId);
    }
    
    public List<Payout> getPayoutsByBenefitEstimationId(Long benefitEstimationId) {
        return payoutRepository.findByBenefitEstimationId(benefitEstimationId);
    }
    
    public List<Payout> getPayoutsByPayoutMethod(PayoutMethod payoutMethod) {
        return payoutRepository.findByPayoutMethod(payoutMethod);
    }
    
    public List<Payout> getPayoutsByStatus(PayoutStatus status) {
        return payoutRepository.findByStatus(status);
    }
    
    public List<Payout> getPayoutsByCustomerAndStatus(String customerId, PayoutStatus status) {
        return payoutRepository.findByCustomerIdAndStatus(customerId, status);
    }
    
    public List<Payout> getPayoutsByStatusAndMethod(PayoutStatus status, PayoutMethod payoutMethod) {
        return payoutRepository.findByStatusAndPayoutMethod(status, payoutMethod);
    }
}
