package com.ilyap.loggingstarter.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The {@code LoggingProperties} is a configuration properties for common logging AOP.
 * It finds properties from the "application.logging" in the application's properties file.
 */
@Slf4j
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "application.logging")
public class LoggingProperties {

    /**
     * Whether to enable Logging support.
     */
    private boolean enabled;

    @PostConstruct
    void init() {
        log.info("Logging properties initialized: {}", this);
    }
}
