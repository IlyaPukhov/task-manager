package com.ilyap.userservice.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserReadDto {

    private long id;
    private String username;
    private String role;
    private List<Long> tasksIds;
}