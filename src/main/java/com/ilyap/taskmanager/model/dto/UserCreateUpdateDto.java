package com.ilyap.taskmanager.model.dto;

import com.ilyap.taskmanager.model.entity.Role;
import com.ilyap.taskmanager.validation.ValueOfEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @ValueOfEnum(enumClass = Role.class)
    String role;

    List<Long> tasksId = new ArrayList<>();
}
