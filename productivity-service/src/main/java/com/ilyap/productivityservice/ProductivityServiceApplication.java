package com.ilyap.productivityservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProductivityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductivityServiceApplication.class, args);
    }

}
