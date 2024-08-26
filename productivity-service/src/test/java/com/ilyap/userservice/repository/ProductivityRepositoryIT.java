package com.ilyap.userservice.repository;

import com.ilyap.productivityservice.repository.ProductivityRepository;
import com.ilyap.userservice.IntegrationTestBase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class ProductivityRepositoryIT extends IntegrationTestBase {

    private final ProductivityRepository productivityRepository;

}
