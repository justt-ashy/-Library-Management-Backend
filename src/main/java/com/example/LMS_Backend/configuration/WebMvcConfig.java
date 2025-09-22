package com.example.LMS_Backend.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * WebMvcConfig:- This class is used to define web-related configurations for the application.
    Here, we mainly configure CORS settings for frontend integration.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // Password Encoder Bean is defined in SecurityConfiguration.java to avoid duplication

    /**
     * CORS Configuration for Frontend Integration
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/rest/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:3001", "http://localhost:8080")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
