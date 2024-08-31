package com.ilyap.productivityservice.controller;

import com.ilyap.productivityservice.IntegrationTestBase;
import com.ilyap.productivityservice.annotation.ControllerIT;
import lombok.RequiredArgsConstructor;
import org.springframework.test.web.reactive.server.WebTestClient;

@ControllerIT
@RequiredArgsConstructor
class ProductivityListControllerIT extends IntegrationTestBase {

    private final WebTestClient webTestClient;
}
