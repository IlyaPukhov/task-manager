package com.ilyap.taskservice.client;

import com.ilyap.taskservice.model.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8081/api/v1/users")
public interface UserServiceClient {

    @GetMapping("/{username:\\w+}")
    UserResponse findByUsername(@PathVariable String username);
}
