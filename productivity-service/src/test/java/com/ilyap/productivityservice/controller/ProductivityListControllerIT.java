package com.ilyap.productivityservice.controller;

import com.ilyap.productivityservice.IntegrationTestBase;
import com.ilyap.productivityservice.annotation.WebFluxControllerIT;
import com.ilyap.productivityservice.cache.HazelcastReactiveCache;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    private final String EXISTS_CREATE_REQUEST_BODY = """
            {
                "username": "norris",
                "date": "2024-08-26",
                "mood": 8,
                "productivity_status": "GOOD",
                "checklist": {
                  "EXERCISE": true,
                  "FAMILY_TIME": false,
                  "WORK": true
                },
                "notes": "Highly productive day, all activities were completed."
            }""";

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
                        }]""", true);
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

    @Test
    void createProductivity_newProductivity_returnsCreatedProductivity() {
        var newProductivityUsername = "newNorris";
        var requestBody = """
                {
                    "username": "newNorris",
                    "date": "2024-08-31",
                    "mood": 8,
                    "productivity_status": "GOOD",
                    "checklist": {
                      "EXERCISE": true,
                      "FAMILY_TIME": false,
                      "WORK": true
                    },
                    "notes": "Highly productive day, all activities were completed."
                }""";

        webTestClient.mutateWith(mockJwt().jwt(builder -> builder.subject(newProductivityUsername)))
                .post()
                .uri("/api/v1/productivity")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody().json("""
                        {
                          "username": "newNorris",
                          "date": "2024-08-31",
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
                        }""");
    }

    @Test
    void createProductivity_productivityAlreadyExists_returnsConflict() {
        webTestClient.mutateWith(mockJwt().jwt(builder -> builder.subject(USERNAME)))
                .post()
                .uri("/api/v1/productivity")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(EXISTS_CREATE_REQUEST_BODY)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody().jsonPath("$.detail").isEqualTo("A productivity entry with the same date already exists.");
    }

    @Test
    void createProductivity_invalidPayload_returnsCreatedProductivity() {
        var invalidRequestBody = """
                {
                    "username": "noNorris",
                    "date": "2024-08-31",
                    "mood": -100000
                }""";

        webTestClient.mutateWith(mockJwt())
                .post()
                .uri("/api/v1/productivity")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequestBody)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody().jsonPath("$.detail").isEqualTo("Invalid request content.");
    }

    @Test
    void createProductivity_userIsNotOwner_returnsCreatedProductivity() {
        webTestClient.mutateWith(mockJwt().jwt(builder -> builder.subject("noNorris")))
                .post()
                .uri("/api/v1/productivity")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(EXISTS_CREATE_REQUEST_BODY)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void deleteAllByUser_ownerIsAuthorized_returnsNoContent() {
        webTestClient.mutateWith(mockJwt().jwt(builder -> builder.subject(USERNAME)))
                .delete()
                .uri("/api/v1/productivity/user/{username}", USERNAME)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void deleteAllByUser_userIsNotOwner_returnsForbidden() {
        webTestClient.mutateWith(mockJwt().jwt(builder -> builder.subject("noNorris")))
                .delete()
                .uri("/api/v1/productivity/user/{username}", USERNAME)
                .exchange()
                .expectStatus().isForbidden();
    }
}
