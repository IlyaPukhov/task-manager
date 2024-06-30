package com.ilyap.loggingstarter.config;

import com.ilyap.loggingstarter.aop.MethodLoggingAspect;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Logging.
 * <p>
 * Auto-configuration classes are regular Spring {@link Configuration @Configuration}
 * beans. Generally auto-configuration beans are {@link Conditional @Conditional} beans (most often using
 * {@link ConditionalOnClass @ConditionalOnClass}, {@link ConditionalOnProperty @ConditionalOnProperty} and
 * {@link ConditionalOnMissingBean @ConditionalOnMissingBean} annotations).
 * </p>
 *
 * @see Conditional
 * @see ConditionalOnClass
 * @see ConditionalOnProperty
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
@ConditionalOnClass(LoggingProperties.class)
@ConditionalOnProperty(prefix = "application.logging", name = "enabled", havingValue = "true")
public class LoggingAutoConfiguration {

    @PostConstruct
    void init() {
        log.info("LoggingAutoConfiguration initialized");
    }

    @Bean
    @ConditionalOnMissingBean
    MethodLoggingAspect loggingMethodExecutionAspect() {
        log.info("MethodLoggingAspect bean created");
        return new MethodLoggingAspect();
    }
}
