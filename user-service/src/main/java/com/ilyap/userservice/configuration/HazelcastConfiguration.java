package com.ilyap.userservice.configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizePolicy;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Hazelcast.
 * This class is responsible for configuring the Hazelcast instance and the user cache.
 */
@Configuration
@EnableCaching
public class HazelcastConfiguration {

    /**
     * Creates and configures the Hazelcast instance.
     *
     * @return {@link Config Hazelcast config}
     */
    @Bean
    public Config cacheConfig() {
        Config config = new Config();
        config.setInstanceName("hazelcast-instance");

        config.addMapConfig(new MapConfig()
                .setName("user-cache")
                .setTimeToLiveSeconds(3000)
                .setEvictionConfig(new EvictionConfig()
                        .setSize(200)
                        .setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_SIZE)
                        .setEvictionPolicy(EvictionPolicy.LRU)
                )
        );

        return config;
    }
}
