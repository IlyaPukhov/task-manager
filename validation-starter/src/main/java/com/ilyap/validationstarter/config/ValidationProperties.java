package com.ilyap.validationstarter.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The {@code ValidationProperties} is a configuration properties for common validation.
 * It finds properties from the "application.validation" in the application's properties file.
 */
@Slf4j
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "application.validation")
public class ValidationProperties {

    /**
     * Whether to enable Validation support.
     */
    private boolean enabled;

    @PostConstruct
    void init() {
        log.info("Validation properties initialized: {}", this);
    }
}
