package com.example.payoutservice.controller;

import com.example.payoutservice.dto.PayoutRequestDto;
import com.example.payoutservice.model.Payout;
import com.example.payoutservice.model.PayoutMethod;
import com.example.payoutservice.model.PayoutStatus;
import com.example.payoutservice.service.PayoutService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payouts")
@CrossOrigin(origins = "*")
public class PayoutController {
    
    private final PayoutService payoutService;
    
    @Autowired
    public PayoutController(PayoutService payoutService) {
        this.payoutService = payoutService;
    }
    
    @GetMapping
    public ResponseEntity<List<Payout>> getAllPayouts() {
        List<Payout> payouts = payoutService.getAllPayouts();
        return ResponseEntity.ok(payouts);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Payout> getPayoutById(@PathVariable Long id) {
        Optional<Payout> payout = payoutService.getPayoutById(id);
        return payout.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Payout> createPayout(@Valid @RequestBody PayoutRequestDto request) {
        try {
            Payout createdPayout = payoutService.createPayout(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPayout);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/{id}/process")
    public ResponseEntity<Payout> processPayout(@PathVariable Long id) {
        Optional<Payout> processedPayout = payoutService.processPayout(id);
        return processedPayout.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<Payout> updatePayoutStatus(@PathVariable Long id, @RequestParam PayoutStatus status) {
        Optional<Payout> updatedPayout = payoutService.updatePayoutStatus(id, status);
        return updatedPayout.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayout(@PathVariable Long id) {
        boolean deleted = payoutService.deletePayout(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Payout>> getPayoutsByCustomerId(@PathVariable String customerId) {
        List<Payout> payouts = payoutService.getPayoutsByCustomerId(customerId);
        return ResponseEntity.ok(payouts);
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Payout>> getPayoutsByOrderId(@PathVariable Long orderId) {
        List<Payout> payouts = payoutService.getPayoutsByOrderId(orderId);
        return ResponseEntity.ok(payouts);
    }
    
    @GetMapping("/benefit-estimation/{benefitEstimationId}")
    public ResponseEntity<List<Payout>> getPayoutsByBenefitEstimationId(@PathVariable Long benefitEstimationId) {
        List<Payout> payouts = payoutService.getPayoutsByBenefitEstimationId(benefitEstimationId);
        return ResponseEntity.ok(payouts);
    }
    
    @GetMapping("/method/{payoutMethod}")
    public ResponseEntity<List<Payout>> getPayoutsByPayoutMethod(@PathVariable PayoutMethod payoutMethod) {
        List<Payout> payouts = payoutService.getPayoutsByPayoutMethod(payoutMethod);
        return ResponseEntity.ok(payouts);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payout>> getPayoutsByStatus(@PathVariable PayoutStatus status) {
        List<Payout> payouts = payoutService.getPayoutsByStatus(status);
        return ResponseEntity.ok(payouts);
    }
    
    @GetMapping("/customer/{customerId}/status/{status}")
    public ResponseEntity<List<Payout>> getPayoutsByCustomerAndStatus(@PathVariable String customerId, 
                                                                     @PathVariable PayoutStatus status) {
        List<Payout> payouts = payoutService.getPayoutsByCustomerAndStatus(customerId, status);
        return ResponseEntity.ok(payouts);
    }
    
    @GetMapping("/status/{status}/method/{payoutMethod}")
    public ResponseEntity<List<Payout>> getPayoutsByStatusAndMethod(@PathVariable PayoutStatus status, 
                                                                   @PathVariable PayoutMethod payoutMethod) {
        List<Payout> payouts = payoutService.getPayoutsByStatusAndMethod(status, payoutMethod);
        return ResponseEntity.ok(payouts);
    }
}
