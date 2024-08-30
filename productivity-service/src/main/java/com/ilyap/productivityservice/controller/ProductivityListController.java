package com.ilyap.productivityservice.controller;

import com.ilyap.productivityservice.model.dto.ProductivityCreateUpdateDto;
import com.ilyap.productivityservice.model.dto.ProductivityReadDto;
import com.ilyap.productivityservice.service.ProductivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/productivity")
@RequiredArgsConstructor
public class ProductivityListController {

    private final ProductivityService productivityService;

    @PostMapping
    public Mono<ResponseEntity<ProductivityReadDto>> create(@Validated @RequestBody Mono<ProductivityCreateUpdateDto> createUpdateDto,
                                                            ServerWebExchange exchange) {
        return createUpdateDto
                .flatMap(productivityService::create)
                .map(productivity -> ResponseEntity
                        .created(UriComponentsBuilder.fromUri(exchange.getRequest().getURI())
                                .path("/{productivityId}")
                                .build(productivity.getId()))
                        .body(productivity));
    }

    @GetMapping("/user/{username}")
    public Flux<ProductivityReadDto> findAllByUser(@PathVariable String username,
                                                   @Validated @RequestParam
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayOfMonth) {
        return productivityService.findByUsername(username, dayOfMonth);
    }

    @PreAuthorize("#username == principal.username")
    @DeleteMapping("/user/{username}")
    public Mono<ResponseEntity<Void>> deleteAllByUser(@PathVariable String username) {
        return productivityService.deleteAllByUsername(username)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
