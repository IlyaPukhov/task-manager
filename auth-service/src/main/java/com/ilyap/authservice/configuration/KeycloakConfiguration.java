package com.ilyap.authservice.configuration;

import jakarta.ws.rs.client.ClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfiguration {

    @Value("${keycloak.realm}")
    private String realmName;

    @Bean
    public Keycloak getAdminKeycloakUser(@Value("${keycloak.server-url}") String serverUrl,
                                         @Value("${keycloak.client-id}") String clientId,
                                         @Value("${keycloak.client-secret}") String clientSecret) {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .grantType(OAuth2Constants.CLIENT_SECRET)
                .realm(realmName)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .resteasyClient(ClientBuilder.newClient())
                .build();
    }

    @Bean
    public RealmResource getRealm(Keycloak keycloak) {
        return keycloak.realm(realmName);
    }
}
