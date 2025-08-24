package com.example.backingservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Backing Service API")
                        .version("1.0")
                        .description("Backing Service with Cache, Database, and Message Broker")
                        .contact(new Contact()
                                .name("Backing Service Team")
                                .email("backing-service@example.com")));
    }
}
