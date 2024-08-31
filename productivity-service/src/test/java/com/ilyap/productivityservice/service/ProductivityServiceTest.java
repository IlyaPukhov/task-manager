package com.ilyap.productivityservice.service;

import com.ilyap.productivityservice.cache.HazelcastReactiveCache;
import com.ilyap.productivityservice.exception.ProductivityAlreadyExistsException;
import com.ilyap.productivityservice.exception.ProductivityNotFoundException;
import com.ilyap.productivityservice.mapper.ProductivityCreateUpdateMapper;
import com.ilyap.productivityservice.mapper.ProductivityReadMapper;
import com.ilyap.productivityservice.model.dto.ProductivityCreateUpdateDto;
import com.ilyap.productivityservice.model.dto.ProductivityReadDto;
import com.ilyap.productivityservice.model.entity.Productivity;
import com.ilyap.productivityservice.model.entity.ProductivityStatus;
import com.ilyap.productivityservice.repository.ProductivityRepository;
import com.ilyap.productivityservice.service.impl.ProductivityServiceImpl;
import com.mongodb.DuplicateKeyException;
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
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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
    void findById_productivityExists_returnsAndCachesProductivity() {
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
        verify(productivityRepository, times(1)).findById(any(UUID.class));
        verifyNoMoreInteractions(productivityRepository);
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
        verify(productivityRepository, times(1)).findById(any(UUID.class));
        verifyNoMoreInteractions(productivityRepository);
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

        verify(productivityRepository, times(1)).findAllByUsername(username,
                LocalDate.of(1970, 1, 1), LocalDate.of(1970, 1, 31));
        verifyNoMoreInteractions(productivityRepository);
    }

    @Test
    void create_newProductivity_createsAndCachesProductivity() {
        var productivityCreateUpdateDto = new ProductivityCreateUpdateDto("norris", LocalDate.EPOCH, 8,
                ProductivityStatus.FAIL.toString(), null, "notes");
        doReturn(Mono.empty()).when(hazelcastCache).put(any(UUID.class), any(ProductivityReadDto.class));
        doAnswer(invocation -> Mono.justOrEmpty(invocation.getArguments()[0]))
                .when(productivityRepository).save(any());

        StepVerifier.create(productivityService.create(productivityCreateUpdateDto))
                .assertNext(productivity ->
                        assertThat(productivity)
                                .usingRecursiveComparison()
                                .ignoringFields("id", "checklist")
                                .isEqualTo(EXPECTED_PRODUCTIVITY))
                .verifyComplete();

        verify(productivityRepository, times(1))
                .save(argThat(productivity ->
                        productivity.getUsername().equals(EXPECTED_PRODUCTIVITY.getUsername())
                        && productivity.getDate().equals(EXPECTED_PRODUCTIVITY.getDate())
                        && productivity.getMood().equals(EXPECTED_PRODUCTIVITY.getMood())
                        && productivity.getProductivityStatus().equals(EXPECTED_PRODUCTIVITY.getProductivityStatus())
                        && productivity.getNotes().equals(EXPECTED_PRODUCTIVITY.getNotes()))
                );
        verifyNoMoreInteractions(productivityRepository);
        verify(hazelcastCache, times(1)).put(any(UUID.class), any());
    }

    @Test
    void create_productivityAlreadyExists_throwsException() {
        var productivityCreateUpdateDto = new ProductivityCreateUpdateDto();
        doThrow(DuplicateKeyException.class).when(productivityRepository).save(any());

        StepVerifier.create(productivityService.create(productivityCreateUpdateDto))
                .expectErrorMatches(throwable ->
                        throwable instanceof ProductivityAlreadyExistsException &&
                        throwable.getMessage().equals("A productivity entry with the same username and date already exists."))
                .verify();

        verify(productivityRepository, times(1)).save(any());
        verifyNoMoreInteractions(productivityRepository);
        verify(hazelcastCache, never()).put(any(UUID.class), any());
    }

    @Test
    void update_productivityExists_updatesAndCachesProductivity() {
        var uuid = UUID.fromString("1d8fdf38-514a-464d-8382-d89364979500");
        var productivityCreateUpdateDto = new ProductivityCreateUpdateDto("norris", LocalDate.EPOCH, 8,
                ProductivityStatus.FAIL.toString(), null, "notes");
        doReturn(Mono.empty()).when(hazelcastCache).put(any(UUID.class), any(ProductivityReadDto.class));
        doReturn(Mono.just(new Productivity())).when(productivityRepository).findById(any(UUID.class));
        doAnswer(invocation -> Mono.justOrEmpty(invocation.getArguments()[0]))
                .when(productivityRepository).save(any());

        StepVerifier.create(productivityService.update(uuid, productivityCreateUpdateDto))
                .assertNext(productivity ->
                        assertThat(productivity)
                                .usingRecursiveComparison()
                                .ignoringFields("id", "checklist")
                                .isEqualTo(EXPECTED_PRODUCTIVITY))
                .verifyComplete();

        verify(productivityRepository, times(1))
                .save(argThat(productivity ->
                        productivity.getUsername().equals(EXPECTED_PRODUCTIVITY.getUsername())
                        && productivity.getDate().equals(EXPECTED_PRODUCTIVITY.getDate())
                        && productivity.getMood().equals(EXPECTED_PRODUCTIVITY.getMood())
                        && productivity.getProductivityStatus().equals(EXPECTED_PRODUCTIVITY.getProductivityStatus())
                        && productivity.getNotes().equals(EXPECTED_PRODUCTIVITY.getNotes()))
                );
        verifyNoMoreInteractions(productivityRepository);
        verify(hazelcastCache, times(1)).put(any(UUID.class), any());
    }

    @Test
    void update_productivityNotExists_throwsException() {
        var uuid = UUID.fromString("1d8fdf38-514a-464d-8382-d89364979500");
        var productivityCreateUpdateDto = new ProductivityCreateUpdateDto();
        doReturn(Mono.empty()).when(productivityRepository).findById(any(UUID.class));

        StepVerifier.create(productivityService.update(uuid, productivityCreateUpdateDto))
                .expectErrorMatches(throwable ->
                        throwable instanceof ProductivityNotFoundException &&
                        throwable.getMessage().contains(uuid.toString()))
                .verify();

        verify(productivityRepository, times(1)).findById(any(UUID.class));
        verifyNoMoreInteractions(productivityRepository);
        verify(hazelcastCache, never()).put(any(UUID.class), any());
    }

    @Test
    void deleteById_deletesProductivity() {
        var uuid = UUID.fromString(EXPECTED_PRODUCTIVITY.getId());
        doReturn(Mono.empty()).when(productivityRepository).deleteById(any(UUID.class));
        doReturn(Mono.empty()).when(hazelcastCache).evict(any(UUID.class));

        StepVerifier.create(productivityService.delete(uuid))
                .verifyComplete();

        verify(productivityRepository, times(1)).deleteById(any(UUID.class));
        verifyNoMoreInteractions(productivityRepository);
        verify(hazelcastCache).evict(any(UUID.class));
    }

    @Test
    void deleteAllByUsername_deletesProductivities() {
        var username = EXPECTED_PRODUCTIVITY.getUsername();
        doReturn(Mono.empty()).when(productivityRepository).deleteAllByUsername(any(String.class));
        doReturn(Mono.empty()).when(hazelcastCache).evictAll();

        StepVerifier.create(productivityService.deleteAllByUsername(username))
                .verifyComplete();

        verify(productivityRepository, times(1)).deleteAllByUsername(any(String.class));
        verifyNoMoreInteractions(productivityRepository);
    }
}
