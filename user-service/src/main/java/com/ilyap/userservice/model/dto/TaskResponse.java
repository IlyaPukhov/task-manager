package com.ilyap.userservice.model.dto;

public record TaskResponse(long id,
                           String title,
                           String description,
                           String status,
                           String priority,
                           Long ownerId) {
}
