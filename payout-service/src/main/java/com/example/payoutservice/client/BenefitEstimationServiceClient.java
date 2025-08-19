package com.example.payoutservice.client;

import com.example.payoutservice.dto.BenefitEstimationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class BenefitEstimationServiceClient {
    
    private final WebClient webClient;
    
    public BenefitEstimationServiceClient(@Value("${benefit-estimation-service.url}") String benefitEstimationServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(benefitEstimationServiceUrl)
                .build();
    }
    
    public Mono<BenefitEstimationDto> getBenefitEstimationById(Long estimationId) {
        return webClient.get()
                .uri("/{id}", estimationId)
                .retrieve()
                .bodyToMono(BenefitEstimationDto.class);
    }
}
