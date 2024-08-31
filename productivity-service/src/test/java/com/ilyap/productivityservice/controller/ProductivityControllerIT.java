package com.ilyap.productivityservice.controller;

import com.ilyap.productivityservice.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@Slf4j
@AutoConfigureWebTestClient
@SpringBootTest
@RequiredArgsConstructor
class ProductivityControllerIT extends IntegrationTestBase {

    private final WebTestClient webTestClient;

    private static final UUID EXISTING_UUID = UUID.fromString("e7a1ff78-d6f1-4f77-bd3b-4e8b0b85af0f");
    private static final UUID NONEXISTED_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");

    private Mono<ClientRequest> printRequest(ClientRequest clientRequest) {
        log.info("========== REQUEST ==========");
        log.info("{} {}", clientRequest.method(), clientRequest.url());
        clientRequest.headers().forEach((header, value) -> log.info("{}: {}", header, value));
        log.info("======== END REQUEST ========");
        return Mono.just(clientRequest);
    }

    @Test
    void findById_productivityExists_returnsProductivityResponse() {
        webTestClient.mutateWith(mockJwt().jwt(builder -> builder.subject("norris")))
                .mutate().filter(ExchangeFilterFunction.ofRequestProcessor(this::printRequest)).build()
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
                .mutate().filter(ExchangeFilterFunction.ofRequestProcessor(this::printRequest)).build()
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
                .mutate().filter(ExchangeFilterFunction.ofRequestProcessor(this::printRequest)).build()
                .get()
                .uri("/api/v1/productivity/{productivityId}", EXISTING_UUID)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void findById_userIsNotOwner_returnsForbidden() {
        webTestClient.mutateWith(mockJwt().jwt(builder -> builder.subject("noNorris")))
                .mutate().filter(ExchangeFilterFunction.ofRequestProcessor(this::printRequest)).build()
                .get()
                .uri("/api/v1/productivity/{productivityId}", EXISTING_UUID)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void updateProductivity_productivityExists_returnsUpdatedProductivity() {
        var requestBody = """
                {
                    "username": "norris",
                    "date": "2024-08-26",
                    "mood": 10,
                    "productivity_status": "FAIL",
                    "checklist": {
                      "EXERCISE": true,
                      "FAMILY_TIME": false,
                      "WORK": true,
                      "LEARN": false,
                      "WALK": false,
                      "COOK": false,
                      "HOBBY": false
                    },
                    "notes": "Highly productive day, all activities were completed :)"
                }""";
        webTestClient.mutateWith(mockJwt().jwt(builder -> builder.subject("norris")))
                .mutate().filter(ExchangeFilterFunction.ofRequestProcessor(this::printRequest)).build()
                .put()
                .uri("/api/v1/productivity/{productivityId}", EXISTING_UUID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody().json("""
                        {
                            "id": "e7a1ff78-d6f1-4f77-bd3b-4e8b0b85af0f",
                            "username": "norris",
                            "date": "2024-08-26",
                            "mood": 10,
                            "productivity_status": "FAIL",
                            "checklist": {
                              "EXERCISE": true,
                              "FAMILY_TIME": false,
                              "WORK": true,
                              "LEARN": false,
                              "WALK": false,
                              "COOK": false,
                              "HOBBY": false
                            },
                            "notes": "Highly productive day, all activities were completed :)"
                        }""");
    }

    @Test
    void updateProductivity_productivityNotExists_returnsNotFound() {
        webTestClient.mutateWith(mockJwt().jwt(builder -> builder.subject("noNorris")))
                .mutate().filter(ExchangeFilterFunction.ofRequestProcessor(this::printRequest)).build()
                .put()
                .uri("/api/v1/productivity/{productivityId}", NONEXISTED_UUID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{}")
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody().jsonPath("$.detail").isEqualTo("Productivity with id " + NONEXISTED_UUID + " not found");
    }

    @Test
    void updateProductivity_invalidPayload_returnsBadRequest() {
        var requestBody = """
                {
                    "username": "norris",
                    "date": "2024-08-26",
                    "mood": -100000,
                    "notes": "Highly productive day, all activities were completed :)"
                }""";
        webTestClient.mutateWith(mockJwt().jwt(builder -> builder.subject("norris")))
                .mutate().filter(ExchangeFilterFunction.ofRequestProcessor(this::printRequest)).build()
                .put()
                .uri("/api/v1/productivity/{productivityId}", EXISTING_UUID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody().jsonPath("$.detail").isEqualTo("Failed to bind request");
    }

    @Test
    void updateProductivity_ownerIsNotAuthorized_returnsForbidden() {
        webTestClient.mutateWith(mockJwt().jwt(builder -> builder.subject("noNorris")))
                .mutate().filter(ExchangeFilterFunction.ofRequestProcessor(this::printRequest)).build()
                .put()
                .uri("/api/v1/productivity/{productivityId}", EXISTING_UUID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{}")
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void deleteProductivity_ownerIsAuthorized_returnsNoContent() {
        webTestClient.mutateWith(mockJwt().jwt(builder -> builder.subject("norris")))
                .mutate().filter(ExchangeFilterFunction.ofRequestProcessor(this::printRequest)).build()
                .delete()
                .uri("/api/v1/productivity/{productivityId}", EXISTING_UUID)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void delete_userIsNotOwner_returnsForbidden() {
        webTestClient.mutateWith(mockJwt().jwt(builder -> builder.subject("noNorris")))
                .mutate().filter(ExchangeFilterFunction.ofRequestProcessor(this::printRequest)).build()
                .delete()
                .uri("/api/v1/productivity/{productivityId}", EXISTING_UUID)
                .exchange()
                .expectStatus().isForbidden();
    }
}
