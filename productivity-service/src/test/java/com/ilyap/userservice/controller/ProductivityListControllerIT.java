package com.ilyap.userservice.controller;

import com.ilyap.userservice.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@RequiredArgsConstructor
class ProductivityListControllerIT extends IntegrationTestBase {

    private final WebTestClient webTestClient;
}
