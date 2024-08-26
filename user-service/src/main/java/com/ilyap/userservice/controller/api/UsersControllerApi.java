package com.ilyap.userservice.controller.api;

import com.ilyap.userservice.model.dto.PageResponse;
import com.ilyap.userservice.model.dto.UserCreateUpdateDto;
import com.ilyap.userservice.model.dto.UserReadDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

@Tag(name = "Users", description = "Operations related to users")
public interface UsersControllerApi {

    @Operation(summary = "Get all users", description = "Returns a paginated list of all users")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful retrieval of user list",
                    content = @Content(
                            schema = @Schema(implementation = PageResponse.class)
                    )
            )
    })
    PageResponse<UserReadDto> findAll(@Parameter(description = "Pagination information") Pageable pageable);

    @Operation(summary = "Register a new user", description = "Creates a new user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User created successfully",
                    content = @Content(
                            schema = @Schema(implementation = UserReadDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User already exists",
                    content = @Content(
                            schema = @Schema(implementation = ProblemDetail.class)
                    ))
    })
    ResponseEntity<?> register(@RequestBody UserCreateUpdateDto userCreateUpdateDto, BindingResult bindingResult) throws BindException;

    @Operation(summary = "Find user by task ID", description = "Returns the user who owns the specified task")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful retrieval of user",
                    content = @Content(
                            schema = @Schema(implementation = UserReadDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })
    UserReadDto findByTaskId(@Parameter(description = "ID of the task") Long taskId);
}
