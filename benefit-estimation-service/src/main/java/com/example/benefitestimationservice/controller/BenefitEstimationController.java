package com.example.benefitestimationservice.controller;

import com.example.benefitestimationservice.dto.BenefitEstimationRequestDto;
import com.example.benefitestimationservice.model.BenefitEstimation;
import com.example.benefitestimationservice.model.EstimationStatus;
import com.example.benefitestimationservice.service.BenefitEstimationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Benefit Estimation Management", description = "APIs for managing benefit estimations in the e-commerce platform")
public class BenefitEstimationController {

    private final BenefitEstimationService benefitEstimationService;

    @Autowired
    public BenefitEstimationController(BenefitEstimationService benefitEstimationService) {
        this.benefitEstimationService = benefitEstimationService;
    }

    @GetMapping
    @Operation(summary = "Get all benefit estimations", description = "Retrieve a list of all benefit estimations in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved benefit estimations",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = BenefitEstimation.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<BenefitEstimation>> getAllBenefitEstimations() {
        List<BenefitEstimation> estimations = benefitEstimationService.getAllEstimations();
        return ResponseEntity.ok(estimations);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get benefit estimation by ID", description = "Retrieve a specific benefit estimation by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved benefit estimation"),
        @ApiResponse(responseCode = "404", description = "Benefit estimation not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<BenefitEstimation> getBenefitEstimationById(
            @Parameter(description = "ID of the benefit estimation to retrieve", required = true)
            @PathVariable Long id) {
        Optional<BenefitEstimation> estimation = benefitEstimationService.getEstimationById(id);
        return estimation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new benefit estimation", description = "Create a new benefit estimation in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Benefit estimation created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<BenefitEstimation> createBenefitEstimation(
            @Parameter(description = "Benefit estimation request object", required = true)
            @Valid @RequestBody BenefitEstimationRequestDto request) {
        try {
            BenefitEstimation createdEstimation = benefitEstimationService.createBenefitEstimation(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEstimation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get benefit estimations by order ID", description = "Retrieve all benefit estimations for a specific order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved benefit estimations for order"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<BenefitEstimation>> getBenefitEstimationsByOrderId(
            @Parameter(description = "Order ID to filter by", required = true)
            @PathVariable Long orderId) {
        List<BenefitEstimation> estimations = benefitEstimationService.getEstimationsByOrderId(orderId);
        return ResponseEntity.ok(estimations);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get benefit estimations by status", description = "Retrieve all benefit estimations with a specific status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved benefit estimations by status"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<BenefitEstimation>> getBenefitEstimationsByStatus(
            @Parameter(description = "Status to filter by", required = true)
            @PathVariable String status) {
        List<BenefitEstimation> estimations = benefitEstimationService.getEstimationsByStatus(EstimationStatus.valueOf(status.toUpperCase()));
        return ResponseEntity.ok(estimations);
    }
}
