package com.ilyap.productivityservice.controller;

import com.ilyap.productivityservice.IntegrationTestBase;
import com.ilyap.productivityservice.annotation.ControllerIT;
import com.ilyap.productivityservice.cache.HazelcastReactiveCache;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.UUID;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@ControllerIT
@RequiredArgsConstructor
class ProductivityControllerIT extends IntegrationTestBase {

    private final WebTestClient webTestClient;
    private final HazelcastReactiveCache hazelcastCache;

    private static final UUID EXISTING_UUID = UUID.fromString("e7a1ff78-d6f1-4f77-bd3b-4e8b0b85af0f");
    private static final UUID NONEXISTED_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");

    private static final String VALID_UPDATE_REQUEST_BODY = """
            {
                "username": "norris",
                "date": "2024-08-26",
                "mood": 1,
                "productivity_status": "FAIL",
                "checklist": {
                  "EXERCISE": false,
                  "FAMILY_TIME": false,
                  "WORK": false
                },
                "notes": "..."
            }""";

    @AfterEach
    void tearDown() {
        hazelcastCache.evictAll().block();
    }

    @Test
    void findById_productivityExists_returnsProductivityResponse() {
        webTestClient.mutateWith(mockJwt().jwt(builder -> builder.subject("norris")))
                .get()
                .uri("/api/v1/productivity/{productivityId}", EXISTING_UUID)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody().json("""
                        {
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
                        }""");
    }

    @Test
    void findById_productivityNotExists_returnsNotFound() {
        webTestClient.mutateWith(mockJwt().jwt(builder -> builder.subject("norris")))
                .get()
                .uri("/api/v1/productivity/{productivityId}", NONEXISTED_UUID)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody().jsonPath("$.detail").isEqualTo("Productivity with id " + NONEXISTED_UUID + " not found");
    }

    @Test
    void findById_userIsNotAuthenticated_returnsUnauthorized() {
        webTestClient
                .get()
                .uri("/api/v1/productivity/{productivityId}", EXISTING_UUID)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void findById_userIsNotOwner_returnsForbidden() {
        webTestClient.mutateWith(mockJwt().jwt(builder -> builder.subject("noNorris")))
                .get()
                .uri("/api/v1/productivity/{productivityId}", EXISTING_UUID)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void updateProductivity_productivityExists_returnsUpdatedProductivity() {
        webTestClient.mutateWith(mockJwt().jwt(builder -> builder.subject("norris")))
                .put()
                .uri("/api/v1/productivity/{productivityId}", EXISTING_UUID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(VALID_UPDATE_REQUEST_BODY)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody().json("""
                        {
                            "id": "e7a1ff78-d6f1-4f77-bd3b-4e8b0b85af0f",
                            "username": "norris",
                            "date": "2024-08-26",
                            "mood": 1,
                            "productivity_status": "FAIL",
                            "checklist": {
                              "EXERCISE": false,
                              "FAMILY_TIME": false,
                              "WORK": false,
                              "LEARN": false,
                              "WALK": false,
                              "COOK": false,
                              "HOBBY": false
                            },
                            "notes": "..."
                        }""");
    }

    @Test
    void updateProductivity_productivityNotExists_returnsNotFound() {
        webTestClient.mutateWith(mockJwt().jwt(builder -> builder.subject("noNorris")))
                .put()
                .uri("/api/v1/productivity/{productivityId}", NONEXISTED_UUID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(VALID_UPDATE_REQUEST_BODY)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody().jsonPath("$.detail").isEqualTo("Productivity with id " + NONEXISTED_UUID + " not found");
    }

    @Test
    void updateProductivity_invalidPayload_returnsBadRequest() {
        var invalidRequestBody = """
                {
                    "username": "norris",
                    "date": "2024-08-26",
                    "mood": -100000,
                    "notes": "Highly productive day, all activities were completed :)"
                }""";

        webTestClient.mutateWith(mockJwt().jwt(builder -> builder.subject("norris")))
                .put()
                .uri("/api/v1/productivity/{productivityId}", EXISTING_UUID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidRequestBody)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody().jsonPath("$.detail").isEqualTo("Invalid request content.");
    }

    @Test
    void updateProductivity_ownerIsNotAuthorized_returnsForbidden() {
        webTestClient.mutateWith(mockJwt().jwt(builder -> builder.subject("noNorris")))
                .put()
                .uri("/api/v1/productivity/{productivityId}", EXISTING_UUID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(VALID_UPDATE_REQUEST_BODY)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void deleteProductivity_ownerIsAuthorized_returnsNoContent() {
        webTestClient.mutateWith(mockJwt().jwt(builder -> builder.subject("norris")))
                .delete()
                .uri("/api/v1/productivity/{productivityId}", EXISTING_UUID)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void delete_userIsNotOwner_returnsForbidden() {
        webTestClient.mutateWith(mockJwt().jwt(builder -> builder.subject("noNorris")))
                .delete()
                .uri("/api/v1/productivity/{productivityId}", EXISTING_UUID)
                .exchange()
                .expectStatus().isForbidden();
    }
}
