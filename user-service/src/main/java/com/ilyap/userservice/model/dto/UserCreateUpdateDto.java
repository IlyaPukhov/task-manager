package com.ilyap.userservice.model.dto;

import com.ilyap.userservice.model.entity.Role;
import com.ilyap.validationstarter.annotation.ValueOfEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UserCreateUpdateDto(@NotBlank @NotEmpty @Size(min = 3, max = 64) String username,
                                  @NotBlank @NotEmpty @Size(min = 8, max = 64) String rawPassword,
                                  @Size(max = 256) String biography,
                                  @ValueOfEnum(Role.class) String role, List<Long> tasksIds) {

}
