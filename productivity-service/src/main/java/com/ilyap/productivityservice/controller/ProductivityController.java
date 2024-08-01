package com.ilyap.productivityservice.controller;

import com.ilyap.productivityservice.model.dto.ProductivityCreateUpdateDto;
import com.ilyap.productivityservice.model.dto.ProductivityReadDto;
import com.ilyap.productivityservice.service.ProductivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/productivity/{productivityId}")
@RequiredArgsConstructor
public class ProductivityController {

    private final ProductivityService productivityService;

    @PreAuthorize("@productivityPermissionHandler.isProductivityUser(#productivityId, principal.username)")
    @GetMapping
    public Mono<ProductivityReadDto> findById(@PathVariable UUID productivityId) {
        return productivityService.findById(productivityId);
    }

    @PreAuthorize("@productivityPermissionHandler.isProductivityUser(#productivityId, principal.username)")
    @PutMapping
    public Mono<ProductivityReadDto> update(@PathVariable UUID productivityId,
                                            @Validated ProductivityCreateUpdateDto createUpdateDto) {
        return productivityService.update(productivityId, createUpdateDto);
    }

    @PreAuthorize("@productivityPermissionHandler.isProductivityUser(#productivityId, principal.username)")
    @DeleteMapping
    public Mono<ResponseEntity<Void>> delete(@PathVariable UUID productivityId) {
        return productivityService.delete(productivityId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
