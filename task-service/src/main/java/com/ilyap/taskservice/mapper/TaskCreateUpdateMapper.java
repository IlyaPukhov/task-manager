package com.ilyap.taskservice.mapper;

import com.ilyap.taskservice.model.dto.TaskCreateUpdateDto;
import com.ilyap.taskservice.model.entity.Task;
import com.ilyap.taskservice.model.entity.UserTask;
import com.ilyap.taskservice.repository.UserRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class TaskCreateUpdateMapper implements CreateUpdateMapper<TaskCreateUpdateDto, Task> {

    @Autowired
    private UserRepository userRepository;

    @AfterMapping
    protected void mapUserTasks(TaskCreateUpdateDto taskCreateUpdateDto, @MappingTarget Task task) {
        setTaskOwner(task);
        setUserTasks(taskCreateUpdateDto, task);
    }

    private void setUserTasks(TaskCreateUpdateDto taskCreateUpdateDto, Task task) {
        List<UserTask> userTasks = taskCreateUpdateDto.getUsersIds().stream()
                .map(userId -> userRepository.findById(userId)
                        .orElseThrow())
                .map(user -> new UserTask()
                        .setUser(user)
                        .setTask(task)
                ).toList();

        task.setUserTasks(userTasks);
    }

    private void setTaskOwner(Task task) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        task.setOwner(
                userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User %s not found".formatted(username)))
        );
    }
}
