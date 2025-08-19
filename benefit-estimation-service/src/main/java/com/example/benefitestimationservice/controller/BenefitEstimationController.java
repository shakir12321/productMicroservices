package com.example.benefitestimationservice.controller;

import com.example.benefitestimationservice.dto.BenefitEstimationRequestDto;
import com.example.benefitestimationservice.model.BenefitEstimation;
import com.example.benefitestimationservice.model.BenefitType;
import com.example.benefitestimationservice.model.EstimationStatus;
import com.example.benefitestimationservice.service.BenefitEstimationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/benefit-estimations")
@CrossOrigin(origins = "*")
public class BenefitEstimationController {
    
    private final BenefitEstimationService benefitEstimationService;
    
    @Autowired
    public BenefitEstimationController(BenefitEstimationService benefitEstimationService) {
        this.benefitEstimationService = benefitEstimationService;
    }
    
    @GetMapping
    public ResponseEntity<List<BenefitEstimation>> getAllEstimations() {
        List<BenefitEstimation> estimations = benefitEstimationService.getAllEstimations();
        return ResponseEntity.ok(estimations);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BenefitEstimation> getEstimationById(@PathVariable Long id) {
        Optional<BenefitEstimation> estimation = benefitEstimationService.getEstimationById(id);
        return estimation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<BenefitEstimation> createBenefitEstimation(@Valid @RequestBody BenefitEstimationRequestDto request) {
        try {
            BenefitEstimation createdEstimation = benefitEstimationService.createBenefitEstimation(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEstimation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<BenefitEstimation> updateEstimationStatus(@PathVariable Long id, @RequestParam EstimationStatus status) {
        Optional<BenefitEstimation> updatedEstimation = benefitEstimationService.updateEstimationStatus(id, status);
        return updatedEstimation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstimation(@PathVariable Long id) {
        boolean deleted = benefitEstimationService.deleteEstimation(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BenefitEstimation>> getEstimationsByCustomerId(@PathVariable String customerId) {
        List<BenefitEstimation> estimations = benefitEstimationService.getEstimationsByCustomerId(customerId);
        return ResponseEntity.ok(estimations);
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<BenefitEstimation>> getEstimationsByOrderId(@PathVariable Long orderId) {
        List<BenefitEstimation> estimations = benefitEstimationService.getEstimationsByOrderId(orderId);
        return ResponseEntity.ok(estimations);
    }
    
    @GetMapping("/benefit-type/{benefitType}")
    public ResponseEntity<List<BenefitEstimation>> getEstimationsByBenefitType(@PathVariable BenefitType benefitType) {
        List<BenefitEstimation> estimations = benefitEstimationService.getEstimationsByBenefitType(benefitType);
        return ResponseEntity.ok(estimations);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<BenefitEstimation>> getEstimationsByStatus(@PathVariable EstimationStatus status) {
        List<BenefitEstimation> estimations = benefitEstimationService.getEstimationsByStatus(status);
        return ResponseEntity.ok(estimations);
    }
    
    @GetMapping("/customer/{customerId}/status/{status}")
    public ResponseEntity<List<BenefitEstimation>> getEstimationsByCustomerAndStatus(@PathVariable String customerId, 
                                                                                    @PathVariable EstimationStatus status) {
        List<BenefitEstimation> estimations = benefitEstimationService.getEstimationsByCustomerAndStatus(customerId, status);
        return ResponseEntity.ok(estimations);
    }
}
