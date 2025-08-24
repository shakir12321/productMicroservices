package com.example.payoutservice.controller;

import com.example.payoutservice.dto.PayoutRequestDto;
import com.example.payoutservice.model.Payout;
import com.example.payoutservice.service.PayoutService;
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
@RequestMapping("/api/payouts")
@CrossOrigin(origins = "*")
@Tag(name = "Payout Management", description = "APIs for managing payouts in the e-commerce platform")
public class PayoutController {

    private final PayoutService payoutService;

    @Autowired
    public PayoutController(PayoutService payoutService) {
        this.payoutService = payoutService;
    }

    @GetMapping
    @Operation(summary = "Get all payouts", description = "Retrieve a list of all payouts in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved payouts",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Payout.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Payout>> getAllPayouts() {
        List<Payout> payouts = payoutService.getAllPayouts();
        return ResponseEntity.ok(payouts);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payout by ID", description = "Retrieve a specific payout by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved payout"),
        @ApiResponse(responseCode = "404", description = "Payout not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Payout> getPayoutById(
            @Parameter(description = "ID of the payout to retrieve", required = true)
            @PathVariable Long id) {
        Optional<Payout> payout = payoutService.getPayoutById(id);
        return payout.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new payout", description = "Create a new payout in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Payout created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Payout> createPayout(
            @Parameter(description = "Payout request object", required = true)
            @Valid @RequestBody PayoutRequestDto request) {
        Payout createdPayout = payoutService.createPayout(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPayout);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get payouts by status", description = "Retrieve all payouts with a specific status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved payouts by status"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Payout>> getPayoutsByStatus(
            @Parameter(description = "Payout status to filter by", required = true)
            @PathVariable String status) {
        List<Payout> payouts = payoutService.getPayoutsByStatus(status);
        return ResponseEntity.ok(payouts);
    }

    @GetMapping("/estimation/{estimationId}")
    @Operation(summary = "Get payouts by benefit estimation ID", description = "Retrieve all payouts for a specific benefit estimation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved payouts for estimation"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Payout>> getPayoutsByEstimationId(
            @Parameter(description = "Benefit estimation ID to filter by", required = true)
            @PathVariable Long estimationId) {
        List<Payout> payouts = payoutService.getPayoutsByBenefitEstimationId(estimationId);
        return ResponseEntity.ok(payouts);
    }
}
