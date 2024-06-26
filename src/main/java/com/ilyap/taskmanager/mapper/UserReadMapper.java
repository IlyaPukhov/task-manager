package com.ilyap.taskmanager.mapper;

import com.ilyap.taskmanager.model.dto.UserReadDto;
import com.ilyap.taskmanager.model.entity.TaskManagerUser;
import com.ilyap.taskmanager.model.entity.UserTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import static com.ilyap.taskmanager.model.dto.UserReadDto.Fields.tasksIds;
import static com.ilyap.taskmanager.model.entity.TaskManagerUser.Fields.userTasks;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserReadMapper {

    @Mapping(source = userTasks, target = tasksIds)
    UserReadDto map(TaskManagerUser user);

    default Long fromUserTaskToTaskId(UserTask userTask) {
        return userTask.getTask().getId();
    }
}
