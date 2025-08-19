package com.example.orderservice.service;

import com.example.orderservice.client.ProductServiceClient;
import com.example.orderservice.dto.OrderRequestDto;
import com.example.orderservice.dto.OrderItemRequestDto;
import com.example.orderservice.dto.ProductDto;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.model.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final ProductServiceClient productServiceClient;
    
    @Autowired
    public OrderService(OrderRepository orderRepository, ProductServiceClient productServiceClient) {
        this.orderRepository = orderRepository;
        this.productServiceClient = productServiceClient;
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
    
    @Transactional
    public Order createOrder(OrderRequestDto orderRequest) {
        // Validate products and reserve stock
        List<OrderItem> orderItems = orderRequest.getOrderItems().stream()
                .map(this::createOrderItem)
                .collect(Collectors.toList());
        
        // Calculate total amount
        BigDecimal totalAmount = orderItems.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Create order
        Order order = new Order();
        order.setCustomerName(orderRequest.getCustomerName());
        order.setCustomerEmail(orderRequest.getCustomerEmail());
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.PENDING);
        
        // Set order reference in order items
        orderItems.forEach(item -> item.setOrder(order));
        order.setOrderItems(orderItems);
        
        return orderRepository.save(order);
    }
    
    private OrderItem createOrderItem(OrderItemRequestDto itemRequest) {
        // Get product information from product service
        ProductDto product = productServiceClient.getProductById(itemRequest.getProductId())
                .block();
        
        if (product == null) {
            throw new RuntimeException("Product not found with ID: " + itemRequest.getProductId());
        }
        
        if (product.getStockQuantity() < itemRequest.getQuantity()) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }
        
        // Reserve stock
        Boolean stockReserved = productServiceClient.reserveStock(itemRequest.getProductId(), itemRequest.getQuantity())
                .block();
        
        if (stockReserved == null || !stockReserved) {
            throw new RuntimeException("Failed to reserve stock for product: " + product.getName());
        }
        
        return new OrderItem(
                product.getId(),
                product.getName(),
                itemRequest.getQuantity(),
                product.getPrice()
        );
    }
    
    public Optional<Order> updateOrderStatus(Long id, OrderStatus status) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(status);
                    return orderRepository.save(order);
                });
    }
    
    public boolean deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<Order> getOrdersByCustomerEmail(String email) {
        return orderRepository.findByCustomerEmail(email);
    }
    
    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
    
    public List<Order> searchOrdersByCustomerName(String customerName) {
        return orderRepository.findByCustomerNameContainingIgnoreCase(customerName);
    }
}
