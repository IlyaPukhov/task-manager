package com.ilyap.authservice.service;

import com.ilyap.authservice.dto.AuthorizationResponse;
import com.ilyap.authservice.dto.RegistrationDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {

    AuthorizationResponse authorize(UserDetails userDetails);

    ResponseEntity<?> register(RegistrationDto registrationDto);
}
