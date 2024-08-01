package com.ilyap.taskservice.mapper;

import com.ilyap.taskservice.model.dto.TaskReadDto;
import com.ilyap.taskservice.model.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskReadMapper {

    Task toEntity(TaskReadDto taskReadDto);

    TaskReadDto toDto(Task task);
}
