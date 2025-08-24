package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequestDto;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderStatus;
import com.example.orderservice.service.OrderService;
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
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
@Tag(name = "Order Management", description = "APIs for managing orders in the e-commerce platform")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "Retrieve a list of all orders in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved orders",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Order.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieve a specific order by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved order"),
        @ApiResponse(responseCode = "404", description = "Order not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Order> getOrderById(
            @Parameter(description = "ID of the order to retrieve", required = true)
            @PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new order", description = "Create a new order in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Order> createOrder(
            @Parameter(description = "Order request object", required = true)
            @Valid @RequestBody OrderRequestDto orderRequest) {
        try {
            Order createdOrder = orderService.createOrder(orderRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update order status", description = "Update the status of an existing order")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
        @ApiResponse(responseCode = "404", description = "Order not found"),
        @ApiResponse(responseCode = "400", description = "Invalid status"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Order> updateOrderStatus(
            @Parameter(description = "ID of the order to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "New order status", required = true)
            @RequestParam OrderStatus status) {
        Optional<Order> updatedOrder = orderService.updateOrderStatus(id, status);
        return updatedOrder.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an order", description = "Delete an order by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Order not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteOrder(
            @Parameter(description = "ID of the order to delete", required = true)
            @PathVariable Long id) {
        boolean deleted = orderService.deleteOrder(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/customer/{email}")
    @Operation(summary = "Get orders by customer email", description = "Retrieve all orders for a specific customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved orders by customer email"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Order>> getOrdersByCustomerEmail(
            @Parameter(description = "Customer email to filter by", required = true)
            @PathVariable String email) {
        List<Order> orders = orderService.getOrdersByCustomerEmail(email);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status", description = "Retrieve all orders with a specific status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved orders by status"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Order>> getOrdersByStatus(
            @Parameter(description = "Order status to filter by", required = true)
            @PathVariable String status) {
        List<Order> orders = orderService.getOrdersByStatus(OrderStatus.valueOf(status.toUpperCase()));
        return ResponseEntity.ok(orders);
    }
}
