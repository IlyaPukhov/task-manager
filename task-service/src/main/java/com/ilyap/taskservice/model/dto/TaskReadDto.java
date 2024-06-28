package com.ilyap.taskservice.model.dto;

import lombok.experimental.FieldNameConstants;

import java.util.List;

@FieldNameConstants
public record TaskReadDto(long id,
                          String title,
                          String description,
                          String status,
                          String priority,
                          List<Long> usersIds) {
}
