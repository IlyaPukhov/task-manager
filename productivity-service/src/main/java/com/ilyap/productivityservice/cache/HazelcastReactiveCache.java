package com.ilyap.productivityservice.cache;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.ilyap.productivityservice.model.dto.ProductivityReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class HazelcastReactiveCache {

    private final HazelcastInstance hazelcastInstance;

    private IMap<UUID, ProductivityReadDto> getProductivityCache() {
        return hazelcastInstance.getMap("productivity-cache");
    }

    public Mono<ProductivityReadDto> get(UUID id) {
        return Mono.fromCompletionStage(() -> getProductivityCache().getAsync(id));
    }

    public Mono<Void> put(UUID id, ProductivityReadDto dto) {
        return Mono.fromCompletionStage(() -> getProductivityCache().putAsync(id, dto)).then();
    }

    public Mono<Void> evict(UUID id) {
        return Mono.fromCompletionStage(() -> getProductivityCache().removeAsync(id)).then();
    }
}
