package com.ilyap.taskmanager.mapper;

import com.ilyap.taskmanager.model.dto.TaskCreateUpdateDto;
import com.ilyap.taskmanager.model.entity.Task;
import com.ilyap.taskmanager.model.entity.UserTask;
import com.ilyap.taskmanager.service.UserService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.ilyap.taskmanager.model.entity.Task.Fields.id;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class TaskCreateUpdateMapper {

    @Autowired
    private UserService userService;

    @Mapping(target = id, ignore = true)
    public abstract Task map(TaskCreateUpdateDto taskCreateUpdateDto);

    @Mapping(target = id, ignore = true)
    public abstract Task map(TaskCreateUpdateDto taskCreateUpdateDto, @MappingTarget Task task);

    @AfterMapping
    protected void mapUserTasks(TaskCreateUpdateDto taskCreateUpdateDto, @MappingTarget Task task) {
        List<UserTask> userTasks = taskCreateUpdateDto.getUsersId().stream()
                .map(userService::getUserById)
                .map(user -> UserTask.builder()
                        .user(user)
                        .task(task)
                        .build()
                ).toList();

        task.setUserTasks(userTasks);
    }
}
