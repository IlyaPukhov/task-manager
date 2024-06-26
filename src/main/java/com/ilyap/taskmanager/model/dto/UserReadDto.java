package com.ilyap.taskmanager.model.dto;

import lombok.experimental.FieldNameConstants;

import java.util.List;

@FieldNameConstants
public record UserReadDto(long id,
                          String username,
                          String role,
                          List<Long> tasksIds) {
}