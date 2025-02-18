package com.uniwear.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000", "https://www.frankit.tk", "https://frankit.tk", "https://www.frankit.com", "https://frankit.com").allowedMethods("GET", "POST", "PUT", "DELETE").allowCredentials(true);
    }
}