package com.ilyap.userservice.model.dto;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public record TaskResponse(long id,
                           String title,
                           String description,
                           String status,
                           String priority,
                           Long ownerId) {
}
