package com.ilyap.taskmanager.mapper;

import com.ilyap.taskmanager.model.dto.UserCreateUpdateDto;
import com.ilyap.taskmanager.model.entity.Role;
import com.ilyap.taskmanager.model.entity.TaskManagerUser;
import com.ilyap.taskmanager.model.entity.UserTask;
import com.ilyap.taskmanager.repository.TaskRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserCreateUpdateMapper implements CreateUpdateMapper<UserCreateUpdateDto, TaskManagerUser> {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterMapping
    protected void mapUserTasks(UserCreateUpdateDto userCreateUpdateDto, @MappingTarget TaskManagerUser user) {
        user.setRole(Role.USER);
        encodePassword(user);
        setUserTasks(userCreateUpdateDto, user);
    }

    private void setUserTasks(UserCreateUpdateDto userCreateUpdateDto, TaskManagerUser user) {
        List<UserTask> userTasks = userCreateUpdateDto.getTasksIds().stream()
                .map(taskId -> taskRepository.findById(taskId)
                        .orElseThrow())
                .map(task -> new UserTask()
                        .setUser(user)
                        .setTask(task)
                ).toList();

        user.setUserTasks(userTasks);
    }

    private void encodePassword(TaskManagerUser user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }
}
