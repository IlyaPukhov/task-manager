package com.ilyap.productivityservice.service;

import com.ilyap.productivityservice.model.dto.ProductivityCreateUpdateDto;
import com.ilyap.productivityservice.model.dto.ProductivityReadDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

public interface ProductivityService {

    Mono<ProductivityReadDto> findById(UUID id);

    Flux<ProductivityReadDto> findByUsername(String username, LocalDate dayOfMonth);

    Mono<ProductivityReadDto> create(ProductivityCreateUpdateDto productivityCreateUpdateDto);

    Mono<ProductivityReadDto> update(UUID id, ProductivityCreateUpdateDto productivityCreateUpdateDto);

    Mono<Void> delete(UUID id);

    Mono<Void> deleteAllByUsername(String username);
}
