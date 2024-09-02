package com.ilyap.productivityservice.repository;

import com.ilyap.productivityservice.IntegrationTestBase;
import com.ilyap.productivityservice.model.entity.ActivityType;
import com.ilyap.productivityservice.model.entity.Productivity;
import com.ilyap.productivityservice.model.entity.ProductivityStatus;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@RequiredArgsConstructor
class ProductivityRepositoryIT extends IntegrationTestBase {

    private final ProductivityRepository productivityRepository;

    private static final Map<ActivityType, Boolean> CHECKLIST = Map.of(
            ActivityType.EXERCISE, true,
            ActivityType.FAMILY_TIME, false,
            ActivityType.WORK, true
    );

    private static final Productivity EXPECTED_PRODUCTIVITY = new Productivity(UUID.fromString("e7a1ff78-d6f1-4f77-bd3b-4e8b0b85af0f"),
            "norris", LocalDate.of(2024, 8, 26), 8, ProductivityStatus.GOOD,
            CHECKLIST, "Highly productive day, all activities were completed.");

    @Test
    void findAllByUsername_validIntervalOrder_returnsProductivityFlux() {
        var username = "norris";
        var start = LocalDate.of(2024, 8, 24);
        var end = LocalDate.of(2024, 8, 27);

        var productivityFlux = productivityRepository.findAllByUsername(username, start, end);

        StepVerifier.create(productivityFlux.collectList())
                .assertNext(productivities ->
                        assertThat(productivities).containsOnly(EXPECTED_PRODUCTIVITY)
                ).verifyComplete();
    }

    @Test
    void findAllByUsername_invalidIntervalOrder_returnsProductivityFlux() {
        var username = "norris";
        var start = LocalDate.of(2024, 8, 27);
        var end = LocalDate.of(2024, 8, 24);

        var productivityFlux = productivityRepository.findAllByUsername(username, start, end);

        StepVerifier.create(productivityFlux)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void findAllByUsername_userHasProductivity_returnsProductivityFlux() {
        var username = "norris";

        var productivityFlux = productivityRepository.findAllByUsername(username);

        StepVerifier.create(productivityFlux.collectList())
                .assertNext(productivities ->
                        assertThat(productivities).containsOnly(EXPECTED_PRODUCTIVITY)
                ).verifyComplete();
    }

    @Test
    void findAllByUsername_userDoesNotHaveProductivity_returnsEmptyFlux() {
        var username = "gates";

        var productivityFlux = productivityRepository.findAllByUsername(username);

        StepVerifier.create(productivityFlux)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void findByUsernameAndDate_returnsProductivityMono() {
        var username = "norris";
        var date = LocalDate.of(2024, 8, 26);

        var productivityMono = productivityRepository.findByUsernameAndDate(username, date);

        StepVerifier.create(productivityMono)
                .expectNext(EXPECTED_PRODUCTIVITY)
                .verifyComplete();
    }

    @Test
    void deleteAllByUsername_successDeleted() {
        var username = "norris";

        var deletedMono = productivityRepository.deleteAllByUsername(username);
        var productivityFlux = productivityRepository.findAllByUsername(username);

        StepVerifier.create(deletedMono.thenMany(productivityFlux))
                .expectNextCount(0)
                .verifyComplete();
    }
}
