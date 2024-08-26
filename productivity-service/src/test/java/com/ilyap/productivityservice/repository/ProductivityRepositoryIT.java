package com.ilyap.productivityservice.repository;

import com.ilyap.productivityservice.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

@RequiredArgsConstructor
class ProductivityRepositoryIT extends IntegrationTestBase {

    private final ProductivityRepository productivityRepository;

    @Test 
    void findAllByUsername_returnsProductivityFlux() {
        var expectedUser = new TaskManagerUser(1L, "norris", "Chuck", "Norris",
                                               LocalDate.of(1940, 1, 1), "r5Q9v@example.com", null);
        var username = "norris";
 
        var maybeUser = userRepository.findByUsername(username);

        assertThat(maybeUser).isPresent();
        maybeUser.ifPresent(user -> assertThat(user).isEqualTo(expectedUser));
    }

    @Test
    void findAllByUsername_returnsEmptyFlux() {
        var username = "gates";

        var maybeUser = userRepository.findByUsername(username);

        assertThat(maybeUser).isNotPresent();
    }

    @Test
    void deleteByUsername() {
        var username = "norris";

        StepVerifier.create(
                        productivityRepository.deleteAllByUsername(username)
                                .thenMany(productivityRepository.findAllByUsername(username))
                )
                .expectNextCount(0)
                .verifyComplete();
    }
}
