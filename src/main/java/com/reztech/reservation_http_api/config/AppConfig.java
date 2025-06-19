package com.reztech.reservation_http_api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Application configuration properties
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.config")
public class AppConfig {
    
    private int defaultPageSize = 20;
    
    private int maxPageSize = 100;
    
    private String timezone = "Europe/Istanbul";
} 