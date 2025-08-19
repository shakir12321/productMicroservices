package com.example.benefitestimationservice.client;

import com.example.benefitestimationservice.dto.OrderDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class OrderServiceClient {
    
    private final WebClient webClient;
    
    public OrderServiceClient(@Value("${order-service.url}") String orderServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(orderServiceUrl)
                .build();
    }
    
    public Mono<OrderDto> getOrderById(Long orderId) {
        return webClient.get()
                .uri("/{id}", orderId)
                .retrieve()
                .bodyToMono(OrderDto.class);
    }
}
