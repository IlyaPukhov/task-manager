package com.ilyap.userservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfiguration {

    @Bean
    public JwtDecoder mockJwtDecoder() {
        return mock(JwtDecoder.class);
    }
}