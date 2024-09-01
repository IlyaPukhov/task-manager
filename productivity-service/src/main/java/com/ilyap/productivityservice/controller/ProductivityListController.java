package com.ilyap.productivityservice.controller;

import com.ilyap.productivityservice.model.dto.ProductivityCreateUpdateDto;
import com.ilyap.productivityservice.model.dto.ProductivityReadDto;
import com.ilyap.productivityservice.service.ProductivityService;
import jakarta.validation.constraints.PastOrPresent;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
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

    @GetMapping("/user/{username}")
    public Flux<ProductivityReadDto> findAllByUser(@PathVariable String username,
                                                   @Validated @RequestParam("day_of_month")
                                                   @PastOrPresent @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dayOfMonth) {
        return productivityService.findByUsername(username, dayOfMonth);
    }

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

    @DeleteMapping("/user/{username}")
    public Mono<ResponseEntity<?>> deleteAllByUser(@PathVariable String username,
                                                   JwtAuthenticationToken authentication) {
        return Mono.just(authentication.getName())
                .flatMap(authenticatedUsername -> authenticatedUsername.equals(username)
                        ? productivityService.deleteAllByUsername(username)
                                .then(Mono.just(ResponseEntity.noContent().build()))
                        : Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN))
                );
    }
}
