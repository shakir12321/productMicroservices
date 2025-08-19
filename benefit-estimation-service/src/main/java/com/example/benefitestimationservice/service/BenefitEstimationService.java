package com.example.benefitestimationservice.service;

import com.example.benefitestimationservice.client.OrderServiceClient;
import com.example.benefitestimationservice.dto.BenefitEstimationRequestDto;
import com.example.benefitestimationservice.dto.OrderDto;
import com.example.benefitestimationservice.model.BenefitEstimation;
import com.example.benefitestimationservice.model.BenefitType;
import com.example.benefitestimationservice.model.EstimationStatus;
import com.example.benefitestimationservice.repository.BenefitEstimationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
public class BenefitEstimationService {
    
    private final BenefitEstimationRepository benefitEstimationRepository;
    private final OrderServiceClient orderServiceClient;
    
    @Autowired
    public BenefitEstimationService(BenefitEstimationRepository benefitEstimationRepository, 
                                   OrderServiceClient orderServiceClient) {
        this.benefitEstimationRepository = benefitEstimationRepository;
        this.orderServiceClient = orderServiceClient;
    }
    
    public List<BenefitEstimation> getAllEstimations() {
        return benefitEstimationRepository.findAll();
    }
    
    public Optional<BenefitEstimation> getEstimationById(Long id) {
        return benefitEstimationRepository.findById(id);
    }
    
    @Transactional
    public BenefitEstimation createBenefitEstimation(BenefitEstimationRequestDto request) {
        // Get order details from order service
        OrderDto order = orderServiceClient.getOrderById(request.getOrderId()).block();
        
        if (order == null) {
            throw new RuntimeException("Order not found with ID: " + request.getOrderId());
        }
        
        // Calculate benefit amount based on order total and benefit type
        BigDecimal estimatedBenefitAmount = calculateBenefitAmount(order.getTotalAmount(), 
                                                                 request.getPreferredBenefitType());
        
        // Create benefit estimation
        BenefitEstimation estimation = new BenefitEstimation();
        estimation.setOrderId(request.getOrderId());
        estimation.setCustomerId(request.getCustomerId());
        estimation.setOrderTotalAmount(order.getTotalAmount());
        estimation.setEstimatedBenefitAmount(estimatedBenefitAmount);
        estimation.setBenefitType(request.getPreferredBenefitType());
        estimation.setStatus(EstimationStatus.CALCULATED);
        estimation.setCalculationDetails(generateCalculationDetails(order, estimatedBenefitAmount, 
                                                                   request.getPreferredBenefitType()));
        
        return benefitEstimationRepository.save(estimation);
    }
    
    private BigDecimal calculateBenefitAmount(BigDecimal orderTotal, BenefitType benefitType) {
        BigDecimal benefitPercentage;
        
        switch (benefitType) {
            case CASHBACK:
                benefitPercentage = new BigDecimal("0.05"); // 5% cashback
                break;
            case LOYALTY_POINTS:
                benefitPercentage = new BigDecimal("0.10"); // 10% in loyalty points
                break;
            case DISCOUNT_COUPON:
                benefitPercentage = new BigDecimal("0.15"); // 15% discount coupon
                break;
            case FREE_SHIPPING:
                benefitPercentage = new BigDecimal("0.08"); // 8% equivalent for free shipping
                break;
            case GIFT_CARD:
                benefitPercentage = new BigDecimal("0.12"); // 12% gift card
                break;
            case REFERRAL_BONUS:
                benefitPercentage = new BigDecimal("0.20"); // 20% referral bonus
                break;
            case SEASONAL_OFFER:
                benefitPercentage = new BigDecimal("0.18"); // 18% seasonal offer
                break;
            case FIRST_PURCHASE_BONUS:
                benefitPercentage = new BigDecimal("0.25"); // 25% first purchase bonus
                break;
            default:
                benefitPercentage = new BigDecimal("0.05"); // Default 5%
        }
        
        return orderTotal.multiply(benefitPercentage).setScale(2, RoundingMode.HALF_UP);
    }
    
    private String generateCalculationDetails(OrderDto order, BigDecimal benefitAmount, BenefitType benefitType) {
        return String.format("Order Total: $%s, Benefit Type: %s, Calculated Benefit: $%s, " +
                           "Order Items: %d, Customer: %s", 
                           order.getTotalAmount(), benefitType, benefitAmount, 
                           order.getOrderItems().size(), order.getCustomerName());
    }
    
    public Optional<BenefitEstimation> updateEstimationStatus(Long id, EstimationStatus status) {
        return benefitEstimationRepository.findById(id)
                .map(estimation -> {
                    estimation.setStatus(status);
                    return benefitEstimationRepository.save(estimation);
                });
    }
    
    public boolean deleteEstimation(Long id) {
        if (benefitEstimationRepository.existsById(id)) {
            benefitEstimationRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<BenefitEstimation> getEstimationsByCustomerId(String customerId) {
        return benefitEstimationRepository.findByCustomerId(customerId);
    }
    
    public List<BenefitEstimation> getEstimationsByOrderId(Long orderId) {
        return benefitEstimationRepository.findByOrderId(orderId);
    }
    
    public List<BenefitEstimation> getEstimationsByBenefitType(BenefitType benefitType) {
        return benefitEstimationRepository.findByBenefitType(benefitType);
    }
    
    public List<BenefitEstimation> getEstimationsByStatus(EstimationStatus status) {
        return benefitEstimationRepository.findByStatus(status);
    }
    
    public List<BenefitEstimation> getEstimationsByCustomerAndStatus(String customerId, EstimationStatus status) {
        return benefitEstimationRepository.findByCustomerIdAndStatus(customerId, status);
    }
}
