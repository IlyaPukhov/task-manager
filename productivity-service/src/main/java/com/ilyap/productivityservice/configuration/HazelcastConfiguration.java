package com.ilyap.productivityservice.configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableCaching
public class HazelcastConfiguration {

    @Bean
    public Config cacheConfig() {
        return new Config()
                .setInstanceName("hazelcast-instance")
                .addMapConfig(new MapConfig()
                        .setName("productivity-cache")
                        .setTimeToLiveSeconds(3000)
                        .setEvictionConfig(new EvictionConfig()
                                .setSize(200)
                                .setMaxSizePolicy(MaxSizePolicy.FREE_HEAP_SIZE)
                                .setEvictionPolicy(EvictionPolicy.LRU)
                        )
                );
    }

    @Bean(destroyMethod = "shutdown")
    @Primary
    public HazelcastInstance hazelcastInstance() {
        return Hazelcast.newHazelcastInstance(cacheConfig());
    }
}
