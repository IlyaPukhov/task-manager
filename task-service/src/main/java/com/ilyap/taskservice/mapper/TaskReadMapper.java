package com.ilyap.taskservice.mapper;

import com.ilyap.taskservice.model.dto.TaskReadDto;
import com.ilyap.taskservice.model.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskReadMapper {

    TaskReadDto map(Task task);
}
