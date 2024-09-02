package com.ilyap.apigateway.configuration.cors;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "spring.cors.url")
public class UrlBasedCorsConfigurationProperties {

    private Map<String, CorsConfiguration> configurations = new LinkedHashMap<>();
}
