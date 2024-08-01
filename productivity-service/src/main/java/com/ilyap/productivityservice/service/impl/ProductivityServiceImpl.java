package com.ilyap.productivityservice.service.impl;

import com.ilyap.logging.annotation.Logged;
import com.ilyap.productivityservice.exception.ProductivityNotFoundException;
import com.ilyap.productivityservice.mapper.ProductivityCreateUpdateMapper;
import com.ilyap.productivityservice.mapper.ProductivityReadMapper;
import com.ilyap.productivityservice.model.dto.ProductivityCreateUpdateDto;
import com.ilyap.productivityservice.model.dto.ProductivityReadDto;
import com.ilyap.productivityservice.repository.ProductivityRepository;
import com.ilyap.productivityservice.service.ProductivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Logged
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductivityServiceImpl implements ProductivityService {

    private final ProductivityReadMapper readMapper;
    private final ProductivityCreateUpdateMapper createUpdateMapper;
    private final ProductivityRepository productivityRepository;

    @Override
    public Mono<ProductivityReadDto> findById(UUID id) {
        return productivityRepository.findById(id)
                .map(readMapper::toDto)
                .switchIfEmpty(Mono.error(() -> new ProductivityNotFoundException(id.toString())));
    }

    @Override
    public Flux<ProductivityReadDto> findByUsername(String username, LocalDate dayOfMonth) {
        LocalDate startOfMonth = dayOfMonth.withDayOfMonth(1);
        LocalDate endOfMonth = dayOfMonth.withDayOfMonth(dayOfMonth.lengthOfMonth());

        return productivityRepository.findAllByUsername(username, startOfMonth, endOfMonth)
                .map(readMapper::toDto);
    }

    @Transactional
    @Override
    public Mono<ProductivityReadDto> create(ProductivityCreateUpdateDto productivityCreateUpdateDto) {
        return Mono.just(productivityCreateUpdateDto)
                .map(createUpdateMapper::toEntity)
                .doOnNext(productivity -> productivity.setId(UUID.randomUUID()))
                .flatMap(productivityRepository::save)
                .map(readMapper::toDto);
    }

    @Transactional
    @Override
    public Mono<ProductivityReadDto> update(UUID id, ProductivityCreateUpdateDto productivityCreateUpdateDto) {
        return productivityRepository.findById(id)
                .map(productivity -> createUpdateMapper.toEntity(productivityCreateUpdateDto, productivity))
                .flatMap(productivityRepository::save)
                .map(readMapper::toDto)
                .switchIfEmpty(Mono.error(() -> new ProductivityNotFoundException(id.toString())));
    }

    @Transactional
    @Override
    public Mono<Void> delete(UUID id) {
        return productivityRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Mono<Void> deleteAllByUsername(String username) {
        return productivityRepository.deleteAllByUsername(username);
    }
}
