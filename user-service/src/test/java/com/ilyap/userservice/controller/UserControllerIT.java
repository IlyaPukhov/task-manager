package com.ilyap.userservice.controller;

import com.ilyap.userservice.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserControllerIT extends IntegrationTestBase {

    private final MockMvc mockMvc;
}
