package com.ilyap.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record RegistrationDto(@NotBlank @Size(min = 3, max = 64) String username,
                              @NotBlank @Size(min = 8) String password,
                              @NotBlank @NotEmpty String firstname,
                              @NotBlank @NotEmpty String lastname,
                              @Past @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthdate,
                              @Email String email,
                              @Size(max = 512) String biography) {

}
