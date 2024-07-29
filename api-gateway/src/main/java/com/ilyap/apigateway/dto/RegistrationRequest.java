package com.ilyap.apigateway.dto;

import java.time.LocalDate;

public record RegistrationRequest(String username,
                                  String firstname,
                                  String lastname,
                                  LocalDate birthdate,
                                  String biography) {

}
