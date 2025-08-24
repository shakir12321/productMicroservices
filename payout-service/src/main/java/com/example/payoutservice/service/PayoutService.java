package com.example.payoutservice.service;

import com.example.payoutservice.client.BenefitEstimationServiceClient;
import com.example.payoutservice.dto.PayoutRequestDto;
import com.example.payoutservice.model.Payout;
import com.example.payoutservice.model.PayoutMethod;
import com.example.payoutservice.model.PayoutStatus;
import com.example.payoutservice.repository.PayoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        Map<String, Object> benefitEstimation = benefitEstimationServiceClient
                .getBenefitEstimationById(request.getBenefitEstimationId()).block();
        
        if (benefitEstimation == null) {
            throw new RuntimeException("Benefit estimation not found with ID: " + request.getBenefitEstimationId());
        }
        
        // Create payout
        Payout payout = new Payout();
        payout.setBenefitEstimationId(request.getBenefitEstimationId());
        payout.setCustomerId(request.getCustomerId());
        payout.setOrderId(0L); // We'll get this from benefit estimation if needed
        payout.setPayoutAmount(request.getAmount());
        payout.setPayoutMethod(request.getPayoutMethod());
        payout.setStatus(PayoutStatus.PENDING);
        payout.setReferenceNumber(generateTransactionId());
        payout.setPayoutDate(LocalDateTime.now());
        payout.setTransactionDetails(generateTransactionDetails(benefitEstimation, request));
        
        return payoutRepository.save(payout);
    }
    
    private String generateTransactionId() {
        return "PAYOUT-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }
    
    private String generateTransactionDetails(Map<String, Object> benefitEstimation, PayoutRequestDto request) {
        return String.format("Payout for Benefit Estimation ID: %d, Amount: $%s, Method: %s, Customer: %s", 
                           request.getBenefitEstimationId(), request.getAmount(), request.getPayoutMethod(), 
                           request.getCustomerId());
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
    
    public List<Payout> getPayoutsByBenefitEstimationId(Long benefitEstimationId) {
        return payoutRepository.findByBenefitEstimationId(benefitEstimationId);
    }
    
    public List<Payout> getPayoutsByStatus(String status) {
        return payoutRepository.findByStatus(PayoutStatus.valueOf(status.toUpperCase()));
    }
    
    public List<Payout> getPayoutsByPayoutMethod(PayoutMethod payoutMethod) {
        return payoutRepository.findByPayoutMethod(payoutMethod);
    }
    
    public List<Payout> getPayoutsByCustomerAndStatus(String customerId, PayoutStatus status) {
        return payoutRepository.findByCustomerIdAndStatus(customerId, status);
    }
    
    public List<Payout> getPayoutsByStatusAndMethod(PayoutStatus status, PayoutMethod payoutMethod) {
        return payoutRepository.findByStatusAndPayoutMethod(status, payoutMethod);
    }
}
