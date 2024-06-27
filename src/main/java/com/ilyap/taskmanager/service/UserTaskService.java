package com.ilyap.taskmanager.service;

public interface UserTaskService {

    void addTaskUser(Long taskId, String username);

    void deleteTaskUser(Long taskId, String username);
}
