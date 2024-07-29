package com.ilyap.userservice.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserReadDto {

    private long id;
    private String username;
    private String firstname;
    private String lastname;
    private LocalDate birthdate;
    private String email;
    private String status;
    private String biography;
    private List<Long> tasksIds;
}