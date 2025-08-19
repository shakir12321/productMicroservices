package com.example.orderservice.client;

import com.example.orderservice.dto.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ProductServiceClient {
    
    private final WebClient webClient;
    
    public ProductServiceClient(@Value("${product-service.url}") String productServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(productServiceUrl)
                .build();
    }
    
    public Mono<ProductDto> getProductById(Long productId) {
        return webClient.get()
                .uri("/{id}", productId)
                .retrieve()
                .bodyToMono(ProductDto.class);
    }
    
    public Mono<Boolean> reserveStock(Long productId, Integer quantity) {
        return webClient.post()
                .uri("/{id}/reserve?quantity={quantity}", productId, quantity)
                .retrieve()
                .toBodilessEntity()
                .map(response -> response.getStatusCode().is2xxSuccessful())
                .onErrorReturn(false);
    }
}
