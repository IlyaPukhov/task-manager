package com.ilyap.authservice.controller;

import com.ilyap.authservice.dto.RegistrationDto;
import com.ilyap.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/**")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @RequestMapping
    public ResponseEntity<?> handleAuthRequest(@CurrentSecurityContext SecurityContext securityContext) {
        Authentication authentication = securityContext.getAuthentication();

        return authentication != null
                ? ResponseEntity.ok(authService.authorize((UserDetails) authentication.getPrincipal()))
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/registration")
    public ResponseEntity<?> handleRegisterRequest(RegistrationDto registrationDto) {
        return authService.register(registrationDto);
    }
}
