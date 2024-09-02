package com.ilyap.productivityservice.mapper;

import com.ilyap.productivityservice.model.dto.ProductivityReadDto;
import com.ilyap.productivityservice.model.entity.ActivityType;
import com.ilyap.productivityservice.model.entity.Productivity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.HashMap;
import java.util.Map;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductivityReadMapper {

    Productivity toEntity(ProductivityReadDto productivityReadDto);

    @Mapping(target = "checklist", ignore = true)
    ProductivityReadDto toDto(Productivity productivity);

    @AfterMapping
    default void fillChecklist(Productivity productivity, @MappingTarget ProductivityReadDto productivityReadDto) {
        Map<ActivityType, Boolean> checklist = productivity.getChecklist() != null
                ? productivity.getChecklist()
                : new HashMap<>();

        for (ActivityType activity : ActivityType.values()) {
            checklist.putIfAbsent(activity, false);
        }

        productivityReadDto.setChecklist(checklist);
    }
}
