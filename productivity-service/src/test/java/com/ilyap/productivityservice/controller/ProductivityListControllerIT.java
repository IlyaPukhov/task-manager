package com.ilyap.productivityservice.controller;

import com.ilyap.productivityservice.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureWebTestClient
@Transactional
@RequiredArgsConstructor
class ProductivityListControllerIT extends IntegrationTestBase {

    private final WebTestClient webTestClient;
}
