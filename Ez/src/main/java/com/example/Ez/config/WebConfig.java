package com.example.Ez.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // Jackson configuration is handled by application.properties
    // Spring Boot auto-configures Jackson via spring-boot-starter-web
}
