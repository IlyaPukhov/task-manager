package com.ilyap.taskmanager.mapper;

import com.ilyap.taskmanager.model.dto.TaskReadDto;
import com.ilyap.taskmanager.model.entity.Task;
import com.ilyap.taskmanager.model.entity.UserTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import static com.ilyap.taskmanager.model.dto.TaskReadDto.Fields.usersId;
import static com.ilyap.taskmanager.model.entity.Task.Fields.userTasks;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskReadMapper {

    @Mapping(source = userTasks, target = usersId)
    TaskReadDto map(Task task);

    default Long fromUserTaskToUserId(UserTask userTask) {
        return userTask.getUser().getId();
    }
}
