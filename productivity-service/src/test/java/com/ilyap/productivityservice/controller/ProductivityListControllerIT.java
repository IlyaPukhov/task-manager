package com.ilyap.productivityservice.controller;

import com.ilyap.productivityservice.IntegrationTestBase;
import com.ilyap.productivityservice.annotation.WebFluxControllerIT;
import com.ilyap.productivityservice.cache.HazelcastReactiveCache;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@WebFluxControllerIT
@RequiredArgsConstructor
class ProductivityListControllerIT extends IntegrationTestBase {

    private final WebTestClient webTestClient;
    private final HazelcastReactiveCache hazelcastCache;

    private static final String USERNAME = "norris";

    @AfterEach
    void tearDown() {
        hazelcastCache.evictAll().block();
    }

    @Test
    void findAllByUser_validRequest_returnsProductivities() {
        var dayOfMonth = LocalDate.of(2024, 8, 15);

        webTestClient.mutateWith(mockJwt())
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/productivity/user/{username}")
                        .queryParam("day_of_month", dayOfMonth.toString())
                        .build(USERNAME))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .json("""
                        [{
                            "id": "e7a1ff78-d6f1-4f77-bd3b-4e8b0b85af0f",
                            "username": "norris",
                            "date": "2024-08-26",
                            "mood": 8,
                            "productivity_status": "GOOD",
                            "checklist": {
                              "EXERCISE": true,
                              "FAMILY_TIME": false,
                              "WORK": true,
                              "LEARN": false,
                              "WALK": false,
                              "COOK": false,
                              "HOBBY": false
                            },
                            "notes": "Highly productive day, all activities were completed."
                        }]""");
    }

    @Test
    void findAllByUser_invalidPayload_returnsBadRequest() {
        var invalidDayOfMonth = LocalDate.of(2025, 8, 15);

        webTestClient.mutateWith(mockJwt())
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/productivity/user/{username}")
                        .queryParam("day_of_month", invalidDayOfMonth.toString())
                        .build(USERNAME))
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody().jsonPath("$.detail").isEqualTo("Validation failure");
    }

//    @Test
//    void createProductivity_newProductivity_returnsCreatedProductivity() {
//        var dto = new ProductivityCreateUpdateDto();
//        var readDto = new ProductivityReadDto(UUID.randomUUID(), "testUser", "testTask", LocalDate.now(), "testNote");
//
//        webTestClient.post()
//                .uri("/api/v1/productivity")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(dto)
//                .exchange()
//                .expectStatus().isCreated()
//                .expectHeader().exists(HttpHeaders.LOCATION)
//                .expectBody(ProductivityReadDto.class)
//                .isEqualTo(readDto);
//    }
//
//    @Test
//    void createProductivity_productivityAlreadyExists_returnsCreatedProductivity() {
//        var dto = new ProductivityCreateUpdateDto();
//        var readDto = new ProductivityReadDto(UUID.randomUUID(), "testUser", "testTask", LocalDate.now(), "testNote");
//
//        webTestClient.post()
//                .uri("/api/v1/productivity")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(dto)
//                .exchange()
//                .expectStatus().isCreated()
//                .expectHeader().exists(HttpHeaders.LOCATION)
//                .expectBody(ProductivityReadDto.class)
//                .isEqualTo(readDto);
//    }
//
//    @Test
//    void createProductivity_invalidPayload_returnsCreatedProductivity() {
//        var dto = new ProductivityCreateUpdateDto();
//        var readDto = new ProductivityReadDto(UUID.randomUUID(), "testUser", "testTask", LocalDate.now(), "testNote");
//
//        webTestClient.post()
//                .uri("/api/v1/productivity")
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(dto)
//                .exchange()
//                .expectStatus().isCreated()
//                .expectHeader().exists(HttpHeaders.LOCATION)
//                .expectBody(ProductivityReadDto.class)
//                .isEqualTo(readDto);
//    }
//
//    @Test
//    void deleteAllByUser_validRequest_returnsNoContent() {
//        var username = "testUser";
//
//        webTestClient.delete()
//                .uri("/api/v1/productivity/user/{username}", username)
//                .headers(headers -> headers.setBearerAuth("valid-token"))
//                .exchange()
//                .expectStatus().isNoContent();
//    }
//
//    @Test
//    void deleteAllByUser_invalidUser_returnsForbidden() {
//        var username = "differentUser";
//
//        webTestClient.delete()
//                .uri("/api/v1/productivity/user/{username}", username)
//                .headers(headers -> headers.setBearerAuth("valid-token"))
//                .exchange()
//                .expectStatus().isForbidden();
//    }
}
