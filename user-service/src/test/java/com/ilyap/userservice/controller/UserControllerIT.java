package com.ilyap.userservice.controller;

import com.ilyap.userservice.IntegrationTestBase;
import com.ilyap.userservice.client.TaskServiceClient;
import com.ilyap.userservice.model.dto.PageResponse;
import com.ilyap.userservice.model.dto.TaskResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class UserControllerIT extends IntegrationTestBase {

    private final MockMvc mockMvc;

    @MockBean
    private TaskServiceClient taskServiceClient;

    @BeforeEach
    void setUp() {
        doReturn(new PageResponse<TaskResponse>(List.of(), null))
                .when(taskServiceClient).findAllTasks(any(String.class));
    }

    @Test
    void findByUsername_userExists_returnsUserResponse() throws Exception {
        var username = "norris";

        mockMvc.perform(get("/api/v1/users/{username}", username))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                  "id": 1,
                                  "username": "norris",
                                  "firstname": "Chuck",
                                  "lastname": "Norris",
                                  "birthdate": "1940-01-01",
                                  "email": "r5Q9v@example.com",
                                  "biography": null,
                                  "tasks_ids":[]
                                }
                                """)
                );
    }

    @Test
    void findByUsername_userNotExists_returnsNotFound() throws Exception {
        var username = "noNorris";

        mockMvc.perform(get("/api/v1/users/{username}", username))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                        jsonPath("$.detail").value("User " + username + " not found")
                );
    }

    @Test
    void updateUserDetails_userExists_returnsUpdatedUser() throws Exception {
        var username = "norris";
        var requestJson = """
                {
                    "username": "norris",
                    "firstname": "Chuck",
                    "lastname": "Norris",
                    "birthdate": "1940-01-01",
                    "email": "r5Q9v@example.com",
                    "biography": null
                }
                """;

        mockMvc.perform(put("/api/v1/users/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(jwt().jwt(builder -> builder.subject(username))))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                {
                                  "id": 1,
                                  "username": "norris",
                                  "firstname": "Chuck",
                                  "lastname": "Norris",
                                  "birthdate": "1940-01-01",
                                  "email": "r5Q9v@example.com",
                                  "biography": null,
                                  "tasks_ids":[]
                                }
                                """)
                );
    }

    @Test
    void updateUserDetails_userNotExists_returnsNotFound() throws Exception {
        var username = "noNorris";
        var requestJson = """
                {
                    "username": "noNorris",
                    "firstname": "Chuck",
                    "lastname": "Norris",
                    "birthdate": "1940-01-01",
                    "email": "r5Q9v@example.com",
                    "biography": null
                }
                """;

        mockMvc.perform(put("/api/v1/users/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(jwt().jwt(builder -> builder.subject(username))))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                        jsonPath("$.detail").value("User " + username + " not found")
                );
    }

    @Test
    void updateUserDetails_userIsNotAuthorized_returnsForbidden() throws Exception {
        var username = "norris";

        mockMvc.perform(put("/api/v1/users/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void updateUserDetails_invalidPayload_returnsBadRequest() throws Exception {
        var username = "norris";
        var invalidRequestJson = """
                {
                    "username": "norris",
                    "firstname": "Chuck",
                    "lastname": "Norris",
                    "birthdate": "1940-01-01",
                    "email": "invalid-email"
                }
                """;

        mockMvc.perform(put("/api/v1/users/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestJson)
                        .with(jwt().jwt(builder -> builder.subject(username))))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                        jsonPath("$.detail").value("Failed to bind request")
                );
    }


    @Test
    void deleteUser_userIsAuthorized_returnsNoContent() throws Exception {
        var username = "norris";

        mockMvc.perform(delete("/api/v1/users/{username}", username)
                        .with(jwt().jwt(builder -> builder.subject(username))))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_userIsNotAuthorized_returnsForbidden() throws Exception {
        var username = "norris";

        mockMvc.perform(delete("/api/v1/users/{username}", username))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
