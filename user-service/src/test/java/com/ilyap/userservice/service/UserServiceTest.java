package com.ilyap.userservice.service;

import com.ilyap.userservice.client.TaskServiceClient;
import com.ilyap.userservice.exception.UserAlreadyExistsException;
import com.ilyap.userservice.exception.UserNotFoundException;
import com.ilyap.userservice.mapper.UserCreateUpdateMapper;
import com.ilyap.userservice.mapper.UserReadMapper;
import com.ilyap.userservice.model.dto.TaskResponse;
import com.ilyap.userservice.model.dto.UserCreateUpdateDto;
import com.ilyap.userservice.model.dto.UserReadDto;
import com.ilyap.userservice.model.entity.User;
import com.ilyap.userservice.repository.UserRepository;
import com.ilyap.userservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
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
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Spy
    private UserCreateUpdateMapper userCreateUpdateMapper = Mappers.getMapper(UserCreateUpdateMapper.class);

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
        var user = new User();
        user.setId(EXPECTED_USER.getId());

        doReturn(new PageImpl<>(List.of(user))).when(userRepository).findAll(any(Pageable.class));
        doReturn(EXPECTED_USER).when(userReadMapper).toDto(any(User.class));

        var result = userService.findAll(Pageable.unpaged());

        assertNotNull(result);
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getId()).isEqualTo(EXPECTED_USER.getId());
        verify(userRepository, times(1)).findAll(any(Pageable.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findOwnerByTaskId_userExists_returnsFoundedUser() {
        var taskId = EXPECTED_USER.getTasksIds().getFirst();

        doReturn(new TaskResponse(taskId, null, null, null, null, "username"))
                .when(taskServiceClient).findTaskByTaskId(taskId);
        doReturn(Optional.of(new User())).when(userRepository).findByUsername("username");
        doReturn(EXPECTED_USER).when(userReadMapper).toDto(any(User.class));

        var result = userService.findOwnerByTaskId(taskId);

        assertNotNull(result);
        assertThat(result).isEqualTo(EXPECTED_USER);
        verify(taskServiceClient).findTaskByTaskId(any(Long.class));
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findOwnerByTaskId_userNotExists_throwsException() {
        var taskId = 0L;

        doReturn(new TaskResponse(taskId, null, null, null, null, "username"))
                .when(taskServiceClient).findTaskByTaskId(taskId);
        doReturn(Optional.empty()).when(userRepository).findByUsername("username");

        assertThatThrownBy(() -> userService.findOwnerByTaskId(taskId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");
        verify(taskServiceClient).findTaskByTaskId(any(Long.class));
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findByUsername_userExists_returnsFoundedUser() {
        doReturn(Optional.of(new User())).when(userRepository).findByUsername("username");
        doReturn(EXPECTED_USER).when(userReadMapper).toDto(any(User.class));

        var result = userService.findByUsername(EXPECTED_USER.getUsername());

        assertNotNull(result);
        assertThat(result).isEqualTo(EXPECTED_USER);
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void findByUsername_userNotExists_throwsException() {
        doReturn(Optional.empty()).when(userRepository).findByUsername("username");

        assertThatThrownBy(() -> userService.findByUsername(EXPECTED_USER.getUsername()))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(EXPECTED_USER.getUsername());
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void createUser_newUser_createsUser() {
        var userCreateUpdateDto = new UserCreateUpdateDto("username", "firstname",
                "lastname", LocalDate.EPOCH, "email", "biography", Collections.singletonList(8L));
        doReturn(Optional.empty())
                .when(userRepository).findByUsername(userCreateUpdateDto.username());
        doAnswer(invocation -> invocation.getArguments()[0]).when(userRepository).save(any());
        doReturn(EXPECTED_USER).when(userReadMapper).toDto(any(User.class));

        var result = userService.create(userCreateUpdateDto);

        assertNotNull(result);
        assertThat(result).isEqualTo(EXPECTED_USER);
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, times(1))
                .save(argThat(user ->
                        user.getUsername().equals(EXPECTED_USER.getUsername())
                        && user.getFirstname().equals(EXPECTED_USER.getFirstname())
                        && user.getLastname().equals(EXPECTED_USER.getLastname())
                        && user.getBirthdate().equals(EXPECTED_USER.getBirthdate())
                        && user.getEmail().equals(EXPECTED_USER.getEmail())
                        && user.getBiography().equals(EXPECTED_USER.getBiography())
                ));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void createUser_userAlreadyExists_throwsException() {
        var userCreateUpdateDto = new UserCreateUpdateDto("username", "firstname",
                "lastname", LocalDate.EPOCH, "email", "biography", Collections.singletonList(8L));
        var user = new User();
        user.setUsername(userCreateUpdateDto.username());
        doReturn(Optional.of(user))
                .when(userRepository).findByUsername(userCreateUpdateDto.username());

        assertThatThrownBy(() -> userService.create(userCreateUpdateDto))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining(user.getUsername());
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, never()).save(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateUser_userExists_updatesUser() {
        var userCreateUpdateDto = new UserCreateUpdateDto("username", "firstname",
                "lastname", LocalDate.EPOCH, "email", "biography", Collections.singletonList(8L));
        doReturn(Optional.of(new User(0L, "username", "firstname", "lastname", LocalDate.EPOCH, "email", "biography")))
                .when(userRepository).findByUsername(userCreateUpdateDto.username());
        doAnswer(invocation -> invocation.getArguments()[0]).when(userRepository).save(any());
        doReturn(EXPECTED_USER).when(userReadMapper).toDto(any(User.class));

        var result = userService.update(userCreateUpdateDto);

        assertNotNull(result);
        assertThat(result).isEqualTo(EXPECTED_USER);
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, times(1))
                .save(argThat(user ->
                        user.getUsername().equals(EXPECTED_USER.getUsername())
                        && user.getFirstname().equals(EXPECTED_USER.getFirstname())
                        && user.getLastname().equals(EXPECTED_USER.getLastname())
                        && user.getBirthdate().equals(EXPECTED_USER.getBirthdate())
                        && user.getEmail().equals(EXPECTED_USER.getEmail())
                        && user.getBiography().equals(EXPECTED_USER.getBiography())
                ));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateUser_userNotExists_throwsException() {
        var userCreateUpdateDto = new UserCreateUpdateDto("username", "firstname",
                "lastname", LocalDate.EPOCH, "email", "biography", Collections.singletonList(8L));
        var username = userCreateUpdateDto.username();
        doReturn(Optional.empty())
                .when(userRepository).findByUsername(username);

        assertThatThrownBy(() -> userService.update(userCreateUpdateDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(username);
   
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, never()).save(any());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deleteUserById_deletesUser() {
        doNothing().when(userRepository).deleteByUsername(any(String.class));

        userService.delete(EXPECTED_USER.getUsername());

        verify(userRepository, times(1)).deleteByUsername(any(String.class));
        verifyNoMoreInteractions(userRepository);
    }
}
