package com.ilyap.apigateway.configuration.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfiguration {

    @Bean
    public CorsWebFilter corsFilter(UrlBasedCorsConfigurationProperties properties) {
        var source = new UrlBasedCorsConfigurationSource();
        properties.getConfigurations().forEach(source::registerCorsConfiguration);
        return new CorsWebFilter(source);
    }
}
