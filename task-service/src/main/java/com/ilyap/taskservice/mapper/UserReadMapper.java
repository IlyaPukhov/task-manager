package com.ilyap.taskservice.mapper;

import com.ilyap.taskservice.model.dto.UserReadDto;
import com.ilyap.taskservice.model.entity.TaskManagerUser;
import com.ilyap.taskservice.model.entity.UserTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import static com.ilyap.taskservice.model.dto.UserReadDto.Fields.tasksIds;
import static com.ilyap.taskservice.model.entity.TaskManagerUser.Fields.userTasks;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserReadMapper {

    @Mapping(source = userTasks, target = tasksIds)
    UserReadDto map(TaskManagerUser user);

    default Long fromUserTaskToTaskId(UserTask userTask) {
        return userTask.getTask().getId();
    }
}
