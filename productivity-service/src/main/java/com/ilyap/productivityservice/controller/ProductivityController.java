package com.ilyap.productivityservice.controller;

import com.ilyap.productivityservice.model.dto.ProductivityCreateUpdateDto;
import com.ilyap.productivityservice.model.dto.ProductivityReadDto;
import com.ilyap.productivityservice.security.ProductivityPermissionHandler;
import com.ilyap.productivityservice.service.ProductivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/productivity/{productivityId}")
@RequiredArgsConstructor
public class ProductivityController {

    private final ProductivityService productivityService;
    private final ProductivityPermissionHandler productivityPermissionHandler;

    @GetMapping
    public Mono<ProductivityReadDto> findById(@PathVariable UUID productivityId,
                                              JwtAuthenticationToken authentication) {
        return productivityPermissionHandler.isProductivityUser(productivityId, authentication.getName())
                .flatMap(isAuthorized -> isAuthorized
                        ? productivityService.findById(productivityId)
                        : Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN))
                );
    }

    @PutMapping
    public Mono<ProductivityReadDto> update(@PathVariable UUID productivityId,
                                            @Validated @RequestBody ProductivityCreateUpdateDto createUpdateDto,
                                            JwtAuthenticationToken authentication) {
        return productivityPermissionHandler.isProductivityUser(productivityId, authentication.getName())
                .flatMap(isAuthorized -> isAuthorized
                        ? productivityService.update(productivityId, createUpdateDto)
                        : Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN))
                );
    }

    @DeleteMapping
    public Mono<ResponseEntity<?>> delete(@PathVariable UUID productivityId,
                                          JwtAuthenticationToken authentication) {
        return productivityPermissionHandler.isProductivityUser(productivityId, authentication.getName())
                .flatMap(isAuthorized -> isAuthorized
                        ? productivityService.delete(productivityId)
                                    .then(Mono.just(ResponseEntity.noContent().build()))
                        : Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN))
                );
    }
}
