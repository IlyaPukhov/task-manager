package com.ilyap.productivityservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ilyap.productivityservice.model.entity.Productivity;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

import java.io.InputStream;
import java.util.List;

@DataMongoTest
@WithMockUser(username = "test@gmail.com", password = "test", authorities = {"ADMIN", "USER"})
public class IntegrationTestBase {

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    private static final MongoDBContainer container = new MongoDBContainer("mongo:8.0.0-rc13");

    @BeforeEach
    @SneakyThrows
    void setUp() {
        InputStream inputStream = new ClassPathResource("mongo/data.json").getInputStream();
        List<Productivity> documents = new ObjectMapper().readValue(inputStream, new TypeReference<>() {});

        mongoTemplate.insertAll(documents).blockLast();
    }

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection(Productivity.class).block();
    }

    @BeforeAll
    static void runContainer() {
        container.start();
    }

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", container::getReplicaSetUrl);
    }
}
