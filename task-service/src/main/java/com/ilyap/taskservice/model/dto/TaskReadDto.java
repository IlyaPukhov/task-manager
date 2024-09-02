package com.ilyap.taskservice.model.dto;

public record TaskReadDto(long id,
                          String title,
                          String description,
                          String status,
                          String priority,
                          String ownerUsername) {
}
