package com.ilyap.taskmanager.mapper;

import com.ilyap.taskmanager.model.dto.TaskDto;
import com.ilyap.taskmanager.model.entity.Task;
import com.ilyap.taskmanager.model.entity.UserTask;
import com.ilyap.taskmanager.service.UserService;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TaskMapper {

    @Autowired
    private UserService userService;

    @Mapping(source = "userTasks", target = "usersId")
    public abstract TaskDto fromEntityToDto(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userTasks", ignore = true)
    @BeanMapping(qualifiedByName = "toEntity")
    public abstract Task fromDtoToEntity(TaskDto taskDto);

    @AfterMapping
    @Named("toEntity")
    protected void mapUserTasks(TaskDto taskDto, @MappingTarget Task task) {
        List<UserTask> userTasks = taskDto.getUsersId().stream()
                .map(userService::getUserById)
                .map(user -> UserTask.builder()
                        .user(user)
                        .task(task)
                        .build()
                ).toList();

        task.setUserTasks(userTasks);
    }

    protected Long fromUserTaskToUserId(UserTask userTask) {
        return userTask.getUser().getId();
    }
}
