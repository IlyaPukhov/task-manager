package com.ilyap.userservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import static org.mockito.Mockito.mock;

@Configuration
public class TestingConfiguration {

    @Bean
    public JwtDecoder jwtDecoder() {
        return mock();
    }
}