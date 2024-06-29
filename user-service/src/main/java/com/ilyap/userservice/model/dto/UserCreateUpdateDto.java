package com.ilyap.userservice.model.dto;

import com.ilyap.userservice.model.entity.Role;
import com.ilyap.userservice.validation.ValueOfEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Value
@AllArgsConstructor
public class UserCreateUpdateDto {

    @NotBlank
    @NotEmpty
    @Size(min = 3, max = 64)
    String username;

    @NotBlank
    @NotEmpty
    @Size(min = 8, max = 64)
    String rawPassword;

    @ValueOfEnum(Role.class)
    String role;

    List<Long> tasksIds = new ArrayList<>();
}
