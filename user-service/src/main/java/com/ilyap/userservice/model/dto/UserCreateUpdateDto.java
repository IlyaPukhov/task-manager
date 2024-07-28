package com.ilyap.userservice.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public record UserCreateUpdateDto(@NotBlank @Size(min = 3, max = 64) String username,
                                  @NotBlank @NotEmpty String firstname,
                                  @NotBlank @NotEmpty String lastname,
                                  @Past @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthdate,
                                  @Size(max = 512) String biography,
                                  List<Long> tasksIds) {

}
