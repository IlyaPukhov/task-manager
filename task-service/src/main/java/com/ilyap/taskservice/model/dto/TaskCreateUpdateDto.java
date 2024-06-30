package com.ilyap.taskservice.model.dto;

import com.ilyap.taskservice.model.entity.Priority;
import com.ilyap.taskservice.model.entity.Status;
import com.ilyap.validationstarter.annotation.ValueOfEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TaskCreateUpdateDto(@NotBlank @NotEmpty @Size(min = 1, max = 64) String title,
                                  @NotBlank @NotEmpty @Size(min = 1, max = 256) String description,
                                  @ValueOfEnum(Status.class) @NotNull String status,
                                  @ValueOfEnum(Priority.class) @NotNull String priority, Long ownerId) {

}
