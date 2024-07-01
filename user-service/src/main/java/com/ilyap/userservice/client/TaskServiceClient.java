package com.ilyap.userservice.client;

import com.ilyap.userservice.model.dto.PageResponse;
import com.ilyap.userservice.model.dto.TaskResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "task-service", url = "http://task-service/api/v1/tasks")
public interface TaskServiceClient {

    @GetMapping("/{taskId:\\d+}")
    TaskResponse findTaskByTaskId(@PathVariable Long taskId);

    @GetMapping("/user/{ownerId:\\d+}")
    PageResponse<TaskResponse> findAllTasks(@PathVariable Long ownerId);
}
