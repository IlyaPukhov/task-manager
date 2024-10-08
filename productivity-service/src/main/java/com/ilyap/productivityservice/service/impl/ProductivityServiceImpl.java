package com.ilyap.productivityservice.service.impl;

import com.ilyap.logging.annotation.Logged;
import com.ilyap.productivityservice.cache.HazelcastReactiveCache;
import com.ilyap.productivityservice.exception.ProductivityAlreadyExistsException;
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
    private final HazelcastReactiveCache hazelcastCache;

    @Override
    public Mono<ProductivityReadDto> findById(UUID id) {
        return hazelcastCache.get(id)
                .switchIfEmpty(
                        productivityRepository.findById(id)
                                .map(readMapper::toDto)
                                .flatMap(fetchedDto ->
                                        hazelcastCache.put(id, fetchedDto)
                                                .thenReturn(fetchedDto)
                                )
                                .switchIfEmpty(Mono.error(new ProductivityNotFoundException(id.toString())))
                );
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
        return productivityRepository.findByUsernameAndDate(productivityCreateUpdateDto.getUsername(), productivityCreateUpdateDto.getDate())
                .flatMap(existingProductivity ->
                        Mono.<ProductivityReadDto>error(new ProductivityAlreadyExistsException("A productivity entry with the same date already exists."))
                ).switchIfEmpty(Mono.just(productivityCreateUpdateDto)
                        .map(createUpdateMapper::toEntity)
                        .doOnNext(productivity -> productivity.setId(UUID.randomUUID()))
                        .flatMap(productivityRepository::save)
                        .map(readMapper::toDto)
                        .flatMap(fetchedDto ->
                                hazelcastCache.put(UUID.fromString(fetchedDto.getId()), fetchedDto)
                                        .thenReturn(fetchedDto)
                        )
                );
    }

    @Transactional
    @Override
    public Mono<ProductivityReadDto> update(UUID id, ProductivityCreateUpdateDto productivityCreateUpdateDto) {
        return productivityRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductivityNotFoundException(id.toString())))
                .flatMap(existingProductivity -> {
                    createUpdateMapper.toEntity(productivityCreateUpdateDto, existingProductivity);
                    return productivityRepository.save(existingProductivity)
                            .map(readMapper::toDto)
                            .flatMap(updatedDto -> hazelcastCache.put(id, updatedDto).thenReturn(updatedDto));
                });
    }

    @Transactional
    @Override
    public Mono<Void> delete(UUID id) {
        return productivityRepository.deleteById(id)
                .then(hazelcastCache.evict(id));
    }

    @Transactional
    @Override
    public Mono<Void> deleteAllByUsername(String username) {
        return productivityRepository.deleteAllByUsername(username)
                .then(hazelcastCache.evictAll());
    }
}
