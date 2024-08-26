package com.ilyap.userservice.service;

import com.ilyap.userservice.client.TaskServiceClient;
import com.ilyap.userservice.exception.UserAlreadyExistsException;
import com.ilyap.userservice.exception.UserNotFoundException;
import com.ilyap.userservice.mapper.UserCreateUpdateMapper;
import com.ilyap.userservice.mapper.UserReadMapper;
import com.ilyap.userservice.model.dto.TaskResponse;
import com.ilyap.userservice.model.dto.UserCreateUpdateDto;
import com.ilyap.userservice.model.dto.UserReadDto;
import com.ilyap.userservice.model.entity.TaskManagerUser;
import com.ilyap.userservice.repository.UserRepository;
import com.ilyap.userservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Spy
    private UserCreateUpdateMapper userCreateUpdateMapper;

    @Mock
    private UserReadMapper userReadMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskServiceClient taskServiceClient;

    @InjectMocks
    private UserServiceImpl userService;

    private static final UserReadDto EXPECTED_USER = new UserReadDto(1L, "username",
            "firstname", "lastname", LocalDate.EPOCH, "email",
            "biography", Collections.singletonList(8L));

    @Test
    void findAll_returnsAllUsers() {
        var user = new TaskManagerUser();
        user.setId(EXPECTED_USER.getId());

        doReturn(new PageImpl<>(List.of(user))).when(userRepository).findAll(any(Pageable.class));
        doReturn(EXPECTED_USER).when(userReadMapper).toDto(any(TaskManagerUser.class));

        var result = userService.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(EXPECTED_USER.getId());
        verify(userRepository, only()).findAll(any(Pageable.class));
    }

    @Test
    void findOwnerByTaskId_userExists_returnsFoundedUser() {
        var taskId = EXPECTED_USER.getTasksIds().getFirst();

        doReturn(new TaskResponse(taskId, null, null, null, null, "username"))
                .when(taskServiceClient).findTaskByTaskId(taskId);
        doReturn(Optional.of(new TaskManagerUser())).when(userRepository).findByUsername(EXPECTED_USER.getUsername());
        doReturn(EXPECTED_USER).when(userReadMapper).toDto(any(TaskManagerUser.class));

        var result = userService.findOwnerByTaskId(taskId);

        assertNotNull(result);
        assertThat(result).isEqualTo(EXPECTED_USER);
        verify(taskServiceClient).findTaskByTaskId(any(Long.class));
        verify(userRepository, only()).findByUsername(any(String.class));
    }

    @Test
    void findOwnerByTaskId_userNotExists_throwsException() {
        var taskId = 0L;

        doReturn(new TaskResponse(taskId, null, null, null, null, "username"))
                .when(taskServiceClient).findTaskByTaskId(taskId);
        doReturn(Optional.empty()).when(userRepository).findByUsername("username");

        assertThatThrownBy(() -> userService.findOwnerByTaskId(taskId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found");
        verify(taskServiceClient).findTaskByTaskId(any(Long.class));
        verify(userRepository, only()).findByUsername(any(String.class));
    }

    @Test
    void findByUsername_userExists_returnsFoundedUser() {
        doReturn(Optional.of(new TaskManagerUser())).when(userRepository).findByUsername(EXPECTED_USER.getUsername());
        doReturn(EXPECTED_USER).when(userReadMapper).toDto(any(TaskManagerUser.class));

        var result = userService.findByUsername(EXPECTED_USER.getUsername());

        assertNotNull(result);
        assertThat(result).isEqualTo(EXPECTED_USER);
        verify(userRepository, only()).findByUsername(any(String.class));
    }

    @Test
    void findByUsername_userNotExists_throwsException() {
        doReturn(Optional.empty()).when(userRepository).findByUsername(EXPECTED_USER.getUsername());

        assertThatThrownBy(() -> userService.findByUsername(EXPECTED_USER.getUsername()))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(EXPECTED_USER.getUsername());
        verify(userRepository, only()).findByUsername(any(String.class));
    }

    @Test
    void createUser_newUser_Created() {
        var userCreateUpdateDto = new UserCreateUpdateDto(EXPECTED_USER.getUsername(), "firstname",
                "lastname", LocalDate.EPOCH, "email", "biography", Collections.singletonList(8L));
        doReturn(Optional.empty())
                .when(userRepository).findByUsername(userCreateUpdateDto.username());
        doReturn(new TaskManagerUser()).when(userCreateUpdateMapper).toEntity(userCreateUpdateDto);
        doReturn(new TaskManagerUser()).when(userRepository).save(any());
        doReturn(EXPECTED_USER).when(userReadMapper).toDto(any(TaskManagerUser.class));

        var result = userService.create(userCreateUpdateDto);

        assertNotNull(result);
        assertThat(result).isEqualTo(EXPECTED_USER);
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, times(1)).save(any(TaskManagerUser.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void createUser_userAlreadyExists_throwsException() {
        var userCreateUpdateDto = new UserCreateUpdateDto(EXPECTED_USER.getUsername(), "firstname",
                "lastname", LocalDate.EPOCH, "email", "biography", Collections.singletonList(8L));
        var taskManagerUser = new TaskManagerUser();
        taskManagerUser.setUsername(userCreateUpdateDto.username());
        doReturn(Optional.of(taskManagerUser))
                .when(userRepository).findByUsername(userCreateUpdateDto.username());

        assertThatThrownBy(() -> userService.create(userCreateUpdateDto))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining(taskManagerUser.getUsername());
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, times(0)).save(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateUser_userExists_userUpdated() {
        var userCreateUpdateDto = new UserCreateUpdateDto(EXPECTED_USER.getUsername(), "firstname",
                "lastname", LocalDate.EPOCH, "email", "biography", Collections.singletonList(8L));
        doReturn(Optional.of(new TaskManagerUser()))
                .when(userRepository).findByUsername(userCreateUpdateDto.username());
        doReturn(new TaskManagerUser()).when(userCreateUpdateMapper).toEntity(eq(userCreateUpdateDto), any(TaskManagerUser.class));
        doReturn(new TaskManagerUser()).when(userRepository).save(any());
        doReturn(EXPECTED_USER).when(userReadMapper).toDto(any(TaskManagerUser.class));

        var result = userService.update(userCreateUpdateDto);

        assertNotNull(result);
        assertThat(result).isEqualTo(EXPECTED_USER);
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, times(1)).save(any(TaskManagerUser.class));
    }

    @Test
    void updateUser_userNotExists_throwsException() {
        var userCreateUpdateDto = new UserCreateUpdateDto(EXPECTED_USER.getUsername(), "firstname",
                "lastname", LocalDate.EPOCH, "email", "biography", Collections.singletonList(8L));
        var taskManagerUser = new TaskManagerUser();
        taskManagerUser.setUsername(userCreateUpdateDto.username());
        doReturn(Optional.of(taskManagerUser))
                .when(userRepository).findByUsername(userCreateUpdateDto.username());

        assertThatThrownBy(() -> userService.update(userCreateUpdateDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(taskManagerUser.getUsername());
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, times(0)).save(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deleteUser_userDeleted() {
        doNothing().when(userRepository).deleteByUsername(any(String.class));

        userService.delete(EXPECTED_USER.getUsername());

        verify(userRepository, only()).deleteByUsername(any(String.class));
    }
}
