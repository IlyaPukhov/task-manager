package com.ilyap.taskmanager.model.dto;

import com.ilyap.taskmanager.model.entity.Priority;
import com.ilyap.taskmanager.model.entity.Status;
import com.ilyap.taskmanager.validation.ValueOfEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

import java.util.ArrayList;
import java.util.List;

@FieldNameConstants
@Value
@AllArgsConstructor
public class TaskCreateUpdateDto {

    @NotBlank
    @NotEmpty
    @Size(min = 1, max = 64)
    String title;

    @NotBlank
    @NotEmpty
    @Size(min = 1, max = 256)
    String description;

    @NotNull
    @ValueOfEnum(enumClass = Status.class)
    String status;

    @NotNull
    @ValueOfEnum(enumClass = Priority.class)
    String priority;

    List<Long> usersIds = new ArrayList<>();
}
