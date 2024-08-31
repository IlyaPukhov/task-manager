package com.ilyap.productivityservice.controller;

import com.ilyap.productivityservice.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@Slf4j
@AutoConfigureWebTestClient
@SpringBootTest
@RequiredArgsConstructor
class ProductivityListControllerIT extends IntegrationTestBase {

    private final WebTestClient webTestClient;
}
