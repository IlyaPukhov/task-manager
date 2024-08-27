package com.ilyap.productivityservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import static org.mockito.Mockito.mock;

@Configuration
public class TestingConfiguration {

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        return mock(ReactiveJwtDecoder.class);
    }
}