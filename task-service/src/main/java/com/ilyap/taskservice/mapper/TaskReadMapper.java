package com.ilyap.taskservice.mapper;

import com.ilyap.taskservice.model.dto.TaskReadDto;
import com.ilyap.taskservice.model.entity.Task;
import com.ilyap.taskservice.model.entity.UserTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import static com.ilyap.taskservice.model.dto.TaskReadDto.Fields.usersIds;
import static com.ilyap.taskservice.model.entity.Task.Fields.userTasks;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskReadMapper {

    @Mapping(source = userTasks, target = usersIds)
    TaskReadDto map(Task task);

    default Long fromUserTaskToUserId(UserTask userTask) {
        return userTask.getUser().getId();
    }
}
