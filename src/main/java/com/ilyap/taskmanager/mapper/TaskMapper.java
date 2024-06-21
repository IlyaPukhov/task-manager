package com.ilyap.taskmanager.mapper;

import com.ilyap.taskmanager.model.dto.TaskDto;
import com.ilyap.taskmanager.model.entity.Task;
import com.ilyap.taskmanager.model.entity.UserTask;
import lombok.AllArgsConstructor;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@AllArgsConstructor
public abstract class TaskMapper {

    private final UserService userService;

    @Mapping(source = "userTasks", target = "usersId")
    public abstract TaskDto fromEntityToDto(Task task);

    @BeanMapping(qualifiedByName = "toEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userTasks", ignore = true)
    public abstract Task fromDtoToEntity(TaskDto taskDto);

    Long fromUserTaskToUserId(UserTask userTask) {
        return userTask.getUser().getId();
    }

    @Named("toEntity")
    @AfterMapping
    public void mapUserTasks(TaskDto taskDto, @MappingTarget Task task) {
        List<UserTask> userTasks = taskDto.getUsersId().stream()
                .map(userId -> userService.getUserById(userId))
                .map(user -> UserTask.builder()
                        .user(user)
                        .task(task)
                        .build()
                ).toList();

        task.setUserTasks(userTasks);
    }
}
