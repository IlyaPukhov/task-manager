package com.ilyap.productivityservice.controller;

import com.ilyap.productivityservice.IntegrationTestBase;
import com.ilyap.productivityservice.annotation.ControllerIT;
import com.ilyap.productivityservice.cache.HazelcastReactiveCache;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.springframework.test.web.reactive.server.WebTestClient;

@ControllerIT
@RequiredArgsConstructor
class ProductivityListControllerIT extends IntegrationTestBase {

    private final WebTestClient webTestClient;
    private final HazelcastReactiveCache hazelcastCache;

    @AfterEach
    void tearDown() {
        hazelcastCache.evictAll().block();
    }

}
