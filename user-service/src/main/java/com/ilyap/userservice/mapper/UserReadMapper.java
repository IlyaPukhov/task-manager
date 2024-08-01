package com.ilyap.userservice.mapper;

import com.ilyap.userservice.client.TaskServiceClient;
import com.ilyap.userservice.model.dto.TaskResponse;
import com.ilyap.userservice.model.dto.UserReadDto;
import com.ilyap.userservice.model.entity.TaskManagerUser;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserReadMapper {

    @Autowired
    private TaskServiceClient taskServiceClient;

    public abstract TaskManagerUser toEntity(UserReadDto userReadDto);

    public abstract UserReadDto toDto(TaskManagerUser user);

    @AfterMapping
    protected void addTasksIds(TaskManagerUser user, @MappingTarget UserReadDto userReadDto) {
        List<Long> allTasksIds = taskServiceClient.findAllTasks(user.getUsername())
                .content().stream()
                .map(TaskResponse::id)
                .toList();

        userReadDto.setTasksIds(allTasksIds);
    }
}
