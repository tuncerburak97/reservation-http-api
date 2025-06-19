package com.reztech.reservation_http_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Reservation Management System API")
                        .version("1.0.0")
                        .description("Comprehensive reservation management system API for managing reservations, " +
                                "businesses, users, and availability scheduling. This API provides full CRUD operations " +
                                "for reservation management with advanced time slot handling and availability checking.")
                        .contact(new Contact()
                                .name("RezTech Development Team")
                                .email("dev@reztech.com")
                                .url("https://reztech.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Development Server"),
                        new Server().url("https://api.reztech.com").description("Production Server")))
                .tags(List.of(
                        new Tag().name("Users").description("User management operations"),
                        new Tag().name("Businesses").description("Business management operations"),
                        new Tag().name("Reservations").description("Reservation management operations"),
                        new Tag().name("Reservation Settings").description("Business reservation configuration operations"),
                        new Tag().name("Availability").description("Availability checking and time slot operations"),
                        new Tag().name("Business Availability").description("Business availability rule management")
                ));
    }
} 