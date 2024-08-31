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

    private Mono<ClientRequest> printRequest(ClientRequest clientRequest) {
        log.info("========== REQUEST ==========");
        log.info("{} {}", clientRequest.method(), clientRequest.url());
        clientRequest.headers().forEach((header, value) -> log.info("{}: {}", header, value));
        log.info("======== END REQUEST ========");
        return Mono.just(clientRequest);
    }

    @Test
    void findById_productivityExists_returnsProductivityResponse() {
        var uuid = UUID.fromString("e7a1ff78-d6f1-4f77-bd3b-4e8b0b85af0f");

        webTestClient
                .mutateWith(mockJwt().jwt(builder -> builder.subject("norris")))
                .mutate().filter(ExchangeFilterFunction.ofRequestProcessor(this::printRequest))
                .build()
                .get()
                .uri("/api/v1/productivity/{productivityId}", uuid)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .json("""
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
        var nonExistentUUID = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");

        webTestClient
                .mutateWith(mockJwt().jwt(builder -> builder.subject("norris")))
                .mutate().filter(ExchangeFilterFunction.ofRequestProcessor(this::printRequest))
                .build()
                .get()
                .uri("/api/v1/productivity/{productivityId}", nonExistentUUID)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON)
                .expectBody().jsonPath("$.detail").isEqualTo("Productivity with id " + nonExistentUUID + " not found");
    }

    @Test
    void findById_userIsNotAuthorized_returnsUnauthorized() {
        var uuid = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");

        webTestClient
                .mutate().filter(ExchangeFilterFunction.ofRequestProcessor(this::printRequest))
                .build()
                .get()
                .uri("/api/v1/productivity/{productivityId}", uuid)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON);
    }

    @Test
    void findById_ownerIsNotAuthorized_returnsForbidden() {
        var uuid = UUID.fromString("e7a1ff78-d6f1-4f77-bd3b-4e8b0b85af0f");

        webTestClient
                .mutateWith(mockJwt().jwt(builder -> builder.subject("noNorris")))
                .mutate().filter(ExchangeFilterFunction.ofRequestProcessor(this::printRequest))
                .build()
                .get()
                .uri("/api/v1/productivity/{productivityId}", uuid)
                .exchange()
                .expectStatus().isForbidden()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON);
    }

}
