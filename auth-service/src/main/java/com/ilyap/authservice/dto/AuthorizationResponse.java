package com.ilyap.authservice.dto;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public record AuthorizationResponse(boolean isAuthorized,
                                    String username,
                                    List<GrantedAuthority> authorities) {

}
