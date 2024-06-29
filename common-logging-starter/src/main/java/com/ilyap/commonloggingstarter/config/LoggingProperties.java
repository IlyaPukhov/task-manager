package com.ilyap.commonloggingstarter.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Slf4j
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "application.logging")
public class LoggingProperties {

    private boolean enabled;

    private String level;

    @PostConstruct
    void init() {
        log.info("Logging properties initialized: {}", this);
    }
}
