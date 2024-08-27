package com.ilyap.userservice.client;

import com.ilyap.userservice.model.dto.PageResponse;
import com.ilyap.userservice.model.dto.TaskResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for interacting with the task service.
 * <p>
 * This interface defines the API endpoints for retrieving tasks.
 */
@FeignClient(name = "task-service", path = "/api/v1/tasks")
public interface TaskServiceClient {

    /**
     * Retrieves a task by its ID.
     *
     * @param taskId the ID of the task to retrieve
     * @return {@link TaskResponse}
     */
    @GetMapping("/{taskId:\\d+}")
    TaskResponse findTaskByTaskId(@PathVariable Long taskId);

    /**
     * Retrieves all tasks owned by a specific user.
     *
     * @param ownerUsername the username of the task owner
     * @return a {@link PageResponse} of {@link TaskResponse}
     */
    @GetMapping("/user/{ownerUsername}")
    PageResponse<TaskResponse> findAllTasks(@PathVariable String ownerUsername);
}
