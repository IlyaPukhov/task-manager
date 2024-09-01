package com.ilyap.productivityservice.model.dto;

import com.ilyap.productivityservice.model.entity.ActivityType;
import com.ilyap.productivityservice.model.entity.ProductivityStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductivityReadDto {
    private String id;
    private String username;
    private LocalDate date;
    private Integer mood;
    private ProductivityStatus productivityStatus;
    private Map<ActivityType, Boolean> checklist;
    private String notes;
}
