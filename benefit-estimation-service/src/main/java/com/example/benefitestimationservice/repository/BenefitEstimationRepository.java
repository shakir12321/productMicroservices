package com.example.benefitestimationservice.repository;

import com.example.benefitestimationservice.model.BenefitEstimation;
import com.example.benefitestimationservice.model.BenefitType;
import com.example.benefitestimationservice.model.EstimationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BenefitEstimationRepository extends JpaRepository<BenefitEstimation, Long> {
    
    List<BenefitEstimation> findByCustomerId(String customerId);
    
    List<BenefitEstimation> findByOrderId(Long orderId);
    
    List<BenefitEstimation> findByBenefitType(BenefitType benefitType);
    
    List<BenefitEstimation> findByStatus(EstimationStatus status);
    
    List<BenefitEstimation> findByEstimationDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<BenefitEstimation> findByCustomerIdAndStatus(String customerId, EstimationStatus status);
}
