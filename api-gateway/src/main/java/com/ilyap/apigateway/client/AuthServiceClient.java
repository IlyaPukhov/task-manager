package com.ilyap.apigateway.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "auth-service", path = "/api/v1/")
public interface AuthServiceClient {

}
