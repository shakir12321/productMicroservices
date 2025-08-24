package com.example.payoutservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class BenefitEstimationServiceClient {
    
    private final WebClient webClient;
    private final String baseUrl;
    
    @Autowired
    public BenefitEstimationServiceClient(WebClient webClient, @Value("${benefit-estimation-service.url}") String baseUrl) {
        this.webClient = webClient;
        this.baseUrl = baseUrl;
    }
    
    @SuppressWarnings("unchecked")
    public Mono<Map<String, Object>> getBenefitEstimationById(Long estimationId) {
        return webClient.get()
                .uri(baseUrl + "/{id}", estimationId)
                .retrieve()
                .bodyToMono(Map.class)
                .map(map -> (Map<String, Object>) map);
    }
}
