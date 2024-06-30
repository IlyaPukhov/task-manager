package com.ilyap.taskservice.security;

import com.ilyap.taskservice.client.UserServiceClient;
import com.ilyap.taskservice.model.dto.UserResponse;
import com.ilyap.taskservice.model.entity.Task;
import com.ilyap.taskservice.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class TaskPermissionHandler {

    private final TaskRepository taskRepository;
    private final UserServiceClient userServiceClient;

    public boolean isTaskOwner(Long taskId, String username) {
        Optional<Long> maybeUserId = Optional.ofNullable(userServiceClient.findByUsername(username))
                .map(UserResponse::id);

        Optional<Long> maybeTaskOwnerId = taskRepository.findById(taskId)
                .map(Task::getOwnerId);

        return Stream.of(maybeUserId, maybeTaskOwnerId).allMatch(Optional::isPresent)
                && maybeUserId.get().equals(maybeTaskOwnerId.get());
    }
}
