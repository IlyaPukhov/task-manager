package com.ilyap.productivityservice.service;

import com.ilyap.productivityservice.cache.HazelcastReactiveCache;
import com.ilyap.productivityservice.exception.ProductivityNotFoundException;
import com.ilyap.productivityservice.mapper.ProductivityCreateUpdateMapper;
import com.ilyap.productivityservice.mapper.ProductivityReadMapper;
import com.ilyap.productivityservice.model.dto.ProductivityReadDto;
import com.ilyap.productivityservice.model.entity.Productivity;
import com.ilyap.productivityservice.model.entity.ProductivityStatus;
import com.ilyap.productivityservice.repository.ProductivityRepository;
import com.ilyap.productivityservice.service.impl.ProductivityServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductivityServiceTest {

    @Spy
    private ProductivityCreateUpdateMapper productivityCreateUpdateMapper = Mappers.getMapper(ProductivityCreateUpdateMapper.class);

    @Spy
    private ProductivityReadMapper productivityReadMapper = Mappers.getMapper(ProductivityReadMapper.class);

    @Mock
    private ProductivityRepository productivityRepository;

    @Mock
    private HazelcastReactiveCache hazelcastCache;

    @InjectMocks
    private ProductivityServiceImpl productivityService;

    private static final ProductivityReadDto EXPECTED_PRODUCTIVITY = new ProductivityReadDto(
            "1d8fdf38-514a-464d-8382-d89364979500", "norris", LocalDate.EPOCH, 8,
            ProductivityStatus.FAIL, null, "notes");

    @Test
    void findById_productivityExists_returnsProductivity() {
        var uuid = UUID.fromString(EXPECTED_PRODUCTIVITY.getId());
        doReturn(Mono.empty()).when(hazelcastCache).get(any(UUID.class));
        doReturn(Mono.empty()).when(hazelcastCache).put(any(UUID.class), any(ProductivityReadDto.class));
        doReturn(Mono.just(new Productivity(uuid, "norris", LocalDate.EPOCH, 8, ProductivityStatus.FAIL,
                null, "notes"))
        ).when(productivityRepository).findById(any(UUID.class));

        StepVerifier.create(productivityService.findById(uuid))
                .expectNextMatches(productivity ->
                        productivity.getId().equals(EXPECTED_PRODUCTIVITY.getId())
                        && productivity.getUsername().equals(EXPECTED_PRODUCTIVITY.getUsername())
                        && productivity.getDate().equals(EXPECTED_PRODUCTIVITY.getDate())
                        && productivity.getMood().equals(EXPECTED_PRODUCTIVITY.getMood())
                        && productivity.getProductivityStatus().equals(EXPECTED_PRODUCTIVITY.getProductivityStatus())
                        && productivity.getNotes().equals(EXPECTED_PRODUCTIVITY.getNotes()))
                .verifyComplete();

        verify(hazelcastCache, times(1)).get(any(UUID.class));
        verify(productivityRepository, only()).findById(any(UUID.class));
        verify(hazelcastCache, times(1)).put(any(UUID.class), any());
    }

    @Test
    void findById_productivityNotExists_throwsException() {
        var uuid = UUID.fromString(EXPECTED_PRODUCTIVITY.getId());
        doReturn(Mono.empty()).when(hazelcastCache).get(any(UUID.class));
        doReturn(Mono.empty()).when(productivityRepository).findById(any(UUID.class));

        StepVerifier.create(productivityService.findById(uuid))
                .expectErrorMatches(throwable ->
                        throwable instanceof ProductivityNotFoundException &&
                        throwable.getMessage().contains(uuid.toString()))
                .verify();

        verify(hazelcastCache, times(1)).get(any(UUID.class));
        verify(productivityRepository, only()).findById(any(UUID.class));
        verify(hazelcastCache, never()).put(any(UUID.class), any());
    }

    @Test
    void findByUsername_returnsProductivities() {
        var username = EXPECTED_PRODUCTIVITY.getUsername();
        var day = LocalDate.EPOCH;
        doReturn(Flux.just(new Productivity(UUID.fromString("1d8fdf38-514a-464d-8382-d89364979500"), "norris",
                LocalDate.EPOCH, 8, ProductivityStatus.FAIL, null, "notes"))
        ).when(productivityRepository).findAllByUsername(any(String.class), any(), any());

        StepVerifier.create(productivityService.findByUsername(username, day).collectList())
                .assertNext(productivities ->
                        assertThat(productivities)
                                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("checklist")
                                .containsOnly(EXPECTED_PRODUCTIVITY)
                ).verifyComplete();

        verify(productivityRepository, only()).findAllByUsername(username,
                LocalDate.of(1970, 1, 1), LocalDate.of(1970, 1, 31));
    }

//    @Test
//    void create_createsAndCachesProductivity() {
//        ProductivityCreateUpdateDto createDto = new ProductivityCreateUpdateDto(USERNAME, DATE, "Details");
//        Productivity productivity = new Productivity();
//
//        when(createUpdateMapper.toEntity(any(ProductivityCreateUpdateDto.class))).thenReturn(productivity);
//        when(productivityRepository.save(any(Productivity.class))).thenReturn(Mono.just(productivity));
//        when(readMapper.toDto(any(Productivity.class))).thenReturn(EXPECTED_DTO);
//        when(hazelcastCache.put(eq(PRODUCTIVITY_ID), any(ProductivityReadDto.class))).thenReturn(Mono.empty());
//
//        Mono<ProductivityReadDto> result = productivityService.create(createDto);
//
//        StepVerifier.create(result)
//                .expectNext(EXPECTED_DTO)
//                .verifyComplete();
//
//        verify(createUpdateMapper).toEntity(any(ProductivityCreateUpdateDto.class));
//        verify(productivityRepository).save(any(Productivity.class));
//        verify(readMapper).toDto(any(Productivity.class));
//        verify(hazelcastCache).put(eq(PRODUCTIVITY_ID), eq(EXPECTED_DTO));
//    }
//
//    @Test
//    void update_productivityExists_updatesAndCachesProductivity() {
//        ProductivityCreateUpdateDto updateDto = new ProductivityCreateUpdateDto(USERNAME, DATE, "Updated details");
//        Productivity productivity = new Productivity();
//
//        when(productivityRepository.findById(PRODUCTIVITY_ID)).thenReturn(Mono.just(productivity));
//        when(createUpdateMapper.toEntity(any(ProductivityCreateUpdateDto.class), any(Productivity.class))).thenReturn(productivity);
//        when(productivityRepository.save(any(Productivity.class))).thenReturn(Mono.just(productivity));
//        when(readMapper.toDto(any(Productivity.class))).thenReturn(EXPECTED_DTO);
//        when(hazelcastCache.put(eq(PRODUCTIVITY_ID), any(ProductivityReadDto.class))).thenReturn(Mono.empty());
//
//        Mono<ProductivityReadDto> result = productivityService.update(PRODUCTIVITY_ID, updateDto);
//
//        StepVerifier.create(result)
//                .expectNext(EXPECTED_DTO)
//                .verifyComplete();
//
//        verify(productivityRepository).findById(PRODUCTIVITY_ID);
//        verify(createUpdateMapper).toEntity(any(ProductivityCreateUpdateDto.class), any(Productivity.class));
//        verify(productivityRepository).save(any(Productivity.class));
//        verify(readMapper).toDto(any(Productivity.class));
//        verify(hazelcastCache).put(eq(PRODUCTIVITY_ID), eq(EXPECTED_DTO));
//    }
//
//    @Test
//    void update_productivityNotExists_throwsException() {
//        ProductivityCreateUpdateDto updateDto = new ProductivityCreateUpdateDto(USERNAME, DATE, "Updated details");
//
//        when(productivityRepository.findById(PRODUCTIVITY_ID)).thenReturn(Mono.empty());
//
//        Mono<ProductivityReadDto> result = productivityService.update(PRODUCTIVITY_ID, updateDto);
//
//        StepVerifier.create(result)
//                .expectError(ProductivityNotFoundException.class)
//                .verify();
//
//        verify(productivityRepository).findById(PRODUCTIVITY_ID);
//    }

    @Test
    void deleteById_deletesProductivity() {
        var uuid = UUID.fromString(EXPECTED_PRODUCTIVITY.getId());
        doReturn(Mono.empty()).when(productivityRepository).deleteById(any(UUID.class));
        doReturn(Mono.empty()).when(hazelcastCache).evict(any(UUID.class));

        StepVerifier.create(productivityService.delete(uuid))
                .verifyComplete();

        verify(productivityRepository, only()).deleteById(any(UUID.class));
        verify(hazelcastCache).evict(any(UUID.class));
    }

    @Test
    void deleteAllByUsername_deletesProductivities() {
        var username = EXPECTED_PRODUCTIVITY.getUsername();
        doReturn(Mono.empty()).when(productivityRepository).deleteAllByUsername(any(String.class));

        StepVerifier.create(productivityService.deleteAllByUsername(username))
                .verifyComplete();

        verify(productivityRepository, only()).deleteAllByUsername(any(String.class));
    }
}
