package com.example.payoutservice.repository;

import com.example.payoutservice.model.Payout;
import com.example.payoutservice.model.PayoutMethod;
import com.example.payoutservice.model.PayoutStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PayoutRepository extends JpaRepository<Payout, Long> {
    
    List<Payout> findByCustomerId(String customerId);
    
    List<Payout> findByOrderId(Long orderId);
    
    List<Payout> findByBenefitEstimationId(Long benefitEstimationId);
    
    List<Payout> findByPayoutMethod(PayoutMethod payoutMethod);
    
    List<Payout> findByStatus(PayoutStatus status);
    
    List<Payout> findByPayoutDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Payout> findByCustomerIdAndStatus(String customerId, PayoutStatus status);
    
    List<Payout> findByStatusAndPayoutMethod(PayoutStatus status, PayoutMethod payoutMethod);
}
