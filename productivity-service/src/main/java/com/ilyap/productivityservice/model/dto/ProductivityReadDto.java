package com.ilyap.productivityservice.model.dto;

import com.ilyap.productivityservice.model.entity.ActivityType;
import com.ilyap.productivityservice.model.entity.ProductivityStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class ProductivityReadDto {
    String id;
    String username;
    LocalDate date;
    Integer mood;
    ProductivityStatus productivityStatus;
    Map<ActivityType, Boolean> checklist;
    String notes;
}
