package com.ilyap.userservice.model.dto;

import java.util.List;

public record UserReadDto(long id,
                          String username,
                          String role,
                          List<Long> tasksIds) {
}