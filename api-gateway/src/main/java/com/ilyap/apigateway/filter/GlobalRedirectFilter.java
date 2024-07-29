package com.ilyap.apigateway.filter;

import com.ilyap.apigateway.dto.AuthorizationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GlobalRedirectFilter implements GlobalFilter {

    @Value("${auth-service.uri}")
    private String authServiceUri;

    private final WebClient webClient;

    @Override
    @Order(-1)
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Mono<AuthorizationResponse> responseMono = webClient.get()
                .uri(authServiceUri + "/api/v1/login")
                .retrieve()
                .bodyToMono(AuthorizationResponse.class);

        return responseMono.flatMap(response -> {
            UserDetails userDetails = new User(response.username(), null, response.authorities());
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setAuthenticated(response.isAuthorized());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return chain.filter(exchange);
        });
    }
}
