package com.ilyap.authservice.service.impl;

import com.ilyap.authservice.client.UserServiceClient;
import com.ilyap.authservice.dto.AuthorizationResponse;
import com.ilyap.authservice.dto.RegistrationDto;
import com.ilyap.authservice.dto.VerificationEmailMessage;
import com.ilyap.authservice.mapper.RegistrationMapper;
import com.ilyap.authservice.service.AuthService;
import com.ilyap.authservice.service.producer.EmailSendingProducer;
import com.ilyap.logging.annotation.Logged;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Logged
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RealmResource realmResource;
    private final RegistrationMapper registrationMapper;
    private final UserServiceClient userServiceClient;
    private final EmailSendingProducer emailProducer;

    @Override
    public AuthorizationResponse authorize(UserDetails userDetails) {
        var authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority.class::cast)
                .toList();

        return new AuthorizationResponse(true, userDetails.getUsername(), authorities);
    }

    @Override
    public ResponseEntity<?> register(RegistrationDto registrationDto) {
        UserRepresentation userRepresentation = registrationMapper.toUserRepresentation(registrationDto);

        Response response = realmResource.users().create(userRepresentation);

        if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
            CredentialRepresentation passwordCred = new CredentialRepresentation();
            String userId = CreatedResponseUtil.getCreatedId(response);
            passwordCred.setTemporary(false);
            passwordCred.setType("password");
            passwordCred.setValue(registrationDto.password());
            UserResource userResource = realmResource.users().get(userId);
            userResource.resetPassword(passwordCred);
            userResource.sendVerifyEmail();

            return createUser(registrationDto);
        }

        return ResponseEntity.status(response.getStatus()).build();
    }

    private ResponseEntity<?> createUser(RegistrationDto registrationDto) {
        emailProducer.sendEmail(new VerificationEmailMessage("You are welcome!", "https://google.com"));
        return userServiceClient.register(registrationMapper.toRequest(registrationDto));
    }
}
