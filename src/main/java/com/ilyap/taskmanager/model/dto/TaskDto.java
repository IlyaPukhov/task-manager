package com.ilyap.taskmanager.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    private String title;
    private String description;
    private String status;
    private String priority;
    private List<Long> usersId;
}
