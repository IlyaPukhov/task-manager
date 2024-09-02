package com.ilyap.productivityservice.security;

import com.ilyap.productivityservice.exception.ProductivityNotFoundException;
import com.ilyap.productivityservice.model.entity.Productivity;
import com.ilyap.productivityservice.repository.ProductivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductivityPermissionHandler {

    private final ProductivityRepository productivityRepository;

    public Mono<Boolean> isProductivityUser(UUID id, String username) {
        return productivityRepository.findById(id)
                .map(Productivity::getUsername)
                .map(username::equals)
                .switchIfEmpty(Mono.error(() -> new ProductivityNotFoundException(id.toString())));
    }
}
