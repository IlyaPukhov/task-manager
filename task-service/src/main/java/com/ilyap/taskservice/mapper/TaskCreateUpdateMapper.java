package com.ilyap.taskservice.mapper;

import com.ilyap.taskservice.model.dto.TaskCreateUpdateDto;
import com.ilyap.taskservice.model.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskCreateUpdateMapper {

    Task map(TaskCreateUpdateDto taskCreateUpdateDto);

    Task map(TaskCreateUpdateDto taskCreateUpdateDto, @MappingTarget Task task);
}
