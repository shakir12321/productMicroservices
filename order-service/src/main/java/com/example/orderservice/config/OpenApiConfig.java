package com.example.orderservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI orderServiceOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8082");
        devServer.setDescription("Development server");

        Server dockerServer = new Server();
        dockerServer.setUrl("http://order-service:8082");
        dockerServer.setDescription("Docker server");

        Contact contact = new Contact();
        contact.setEmail("admin@example.com");
        contact.setName("Order Service Team");
        contact.setUrl("https://www.example.com");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Order Service API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage orders in the e-commerce platform.")
                .termsOfService("https://www.example.com/terms")
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, dockerServer));
    }
}


