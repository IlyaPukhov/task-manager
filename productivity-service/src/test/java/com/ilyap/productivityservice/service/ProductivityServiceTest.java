package com.ilyap.productivityservice.service;

import com.ilyap.productivityservice.cache.HazelcastReactiveCache;
import com.ilyap.productivityservice.mapper.ProductivityCreateUpdateMapper;
import com.ilyap.productivityservice.mapper.ProductivityReadMapper;
import com.ilyap.productivityservice.repository.ProductivityRepository;
import com.ilyap.productivityservice.service.impl.ProductivityServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductivityServiceTest {

    @Mock
    private ProductivityReadMapper readMapper;

    @Mock
    private ProductivityCreateUpdateMapper createUpdateMapper;

    @Mock
    private ProductivityRepository productivityRepository;

    @Mock
    private HazelcastReactiveCache hazelcastCache;

    @InjectMocks
    private ProductivityServiceImpl productivityService;
}
