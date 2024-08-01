package com.ilyap.taskservice.mapper;

import com.ilyap.taskservice.model.dto.TaskCreateUpdateDto;
import com.ilyap.taskservice.model.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskCreateUpdateMapper {

    Task toEntity(TaskCreateUpdateDto taskCreateUpdateDto);

    @Mapping(target = "id", ignore = true)
    Task toEntity(TaskCreateUpdateDto taskCreateUpdateDto, @MappingTarget Task task);

    TaskCreateUpdateDto toDto(Task task);
}
