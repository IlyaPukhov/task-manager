package com.ilyap.authservice.client;

import com.ilyap.authservice.dto.RegistrationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service", path = "/api/v1/users")
public interface UserServiceClient {

    @PostMapping("/registration")
    ResponseEntity<?> register(RegistrationRequest registrationRequest);
}
