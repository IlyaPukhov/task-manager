package com.ilyap.taskservice.model.dto;

import java.util.List;

public record UserResponse(long id,
                           String username,
                           String biography,
                           String role,
                           List<Long> tasksIds) {
}