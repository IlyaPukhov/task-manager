package com.ilyap.userservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReadDto {

    private long id;
    private String username;
    private String firstname;
    private String lastname;
    private LocalDate birthdate;
    private String email;
    private String biography;
    private List<Long> tasksIds;
}