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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class UsersControllerIT extends IntegrationTestBase {

    private final MockMvc mockMvc;

    @MockBean
    private TaskServiceClient taskServiceClient;

    @BeforeEach
    void setUp() {
        doReturn(new PageResponse<TaskResponse>(List.of(), null))
                .when(taskServiceClient).findAllTasks(any(String.class));
    }

    @Test
    void findAll_returnsContent() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/api/v1/users")
                .param("page", "0")
                .param("size", "1")
                .param("sort", "id,asc");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        jsonPath("$.content", hasSize(1)),
                        jsonPath("$.content[0].id").value(1),
                        jsonPath("$.content[0].username").value("norris"),
                        jsonPath("$.content[0].firstname").value("Chuck"),
                        jsonPath("$.content[0].lastname").value("Norris"),
                        jsonPath("$.content[0].birthdate").value("1940-01-01"),
                        jsonPath("$.content[0].email").value("r5Q9v@example.com"),
                        jsonPath("$.content[0].biography").value(nullValue()),
                        jsonPath("$.content[0].tasks_ids", hasSize(0))
                );
    }

    @Test
    void findAll_invalidPageNumber_returnsEmptyContent() throws Exception {
        var requestBuilder = MockMvcRequestBuilders.get("/api/v1/users")
                .param("page", "5000000")
                .param("size", "10")
                .param("sort", "id,asc");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        jsonPath("$.content", hasSize(0))
                );
    }

    @Test
    void register() throws Exception {
        var userCreateDto = new UserCreateUpdateDto("norris", "Chuck", "Norris", "1940-01-01", "r5Q9v@example.com", null);
        var userReadDto = new UserReadDto(1L, "norris", "Chuck", "Norris", "1940-01-01", "r5Q9v@example.com", null, List.of());

        doReturn(userReadDto).when(userService).create(any(UserCreateUpdateDto.class));

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

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(1),
                        jsonPath("$.username").value("norris"),
                        jsonPath("$.firstname").value("Chuck"),
                        jsonPath("$.lastname").value("Norris"),
                        jsonPath("$.birthdate").value("1940-01-01"),
                        jsonPath("$.email").value("r5Q9v@example.com"),
                        jsonPath("$.biography").value(nullValue())
                );
    }


    @Test
    void register_invalidData() throws Exception {
        var invalidUserJson = """
                {
                    "username": "",
                    "firstname": "Chuck",
                    "lastname": "Norris",
                    "birthdate": "1940-01-01",
                    "email": "invalid-email"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserJson))
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.error").value("Validation failed"),
                        jsonPath("$.status").value(400)
                );
    }


    @Test
    void findByTaskId_returnsTaskResponse() throws Exception {
        var taskId = 999L;
        doReturn(new TaskResponse(taskId, null, null, null, null, "norris"))
                .when(taskServiceClient).findTaskByTaskId(taskId);

        mockMvc.perform(get("/api/v1/users/tasks/{taskId}", taskId))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        jsonPath("$.id").value(1),
                        jsonPath("$.username").value("norris"),
                        jsonPath("$.firstname").value("Chuck"),
                        jsonPath("$.lastname").value("Norris"),
                        jsonPath("$.birthdate").value("1940-01-01"),
                        jsonPath("$.email").value("r5Q9v@example.com"),
                        jsonPath("$.biography").value(nullValue())
                );
    }

    @Test
    void findByTaskId_ownerUsernameNotFound_returnsNotFound() throws Exception {
        var taskId = 999L;
        doReturn(new TaskResponse(taskId, null, null, null, null, "noNorris"))
                .when(taskServiceClient).findTaskByTaskId(taskId);

        mockMvc.perform(get("/api/v1/users/tasks/{taskId}", taskId))
                .andDo(print())
                .andExpectAll(
                        status().isNotFound(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                        jsonPath("$.detail").value("User not found")
                );
    }
}
