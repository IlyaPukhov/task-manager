package com.ilyap.taskmanager.model.dto;

import com.ilyap.taskmanager.model.entity.Priority;
import com.ilyap.taskmanager.model.entity.Status;
import com.ilyap.taskmanager.validation.ValueOfEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.ArrayList;
import java.util.List;

@FieldNameConstants
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCreateUpdateDto {

    @NotBlank
    @NotEmpty
    @Size(min = 1, max = 64)
    private String title;

    @NotBlank
    @NotEmpty
    @Size(min = 1, max = 256)
    private String description;

    @NotNull
    @ValueOfEnum(enumClass = Status.class)
    private String status;

    @NotNull
    @ValueOfEnum(enumClass = Priority.class)
    private String priority;

    private List<Long> usersId = new ArrayList<>();
}
