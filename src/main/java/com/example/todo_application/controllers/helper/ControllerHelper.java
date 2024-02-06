package com.example.todo_application.controllers.helper;

import com.example.todo_application.entity.TaskListEntity;
import com.example.todo_application.exception.TaskListNotFound;
import com.example.todo_application.repository.TaskListRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@Transactional
@RequiredArgsConstructor
public class ControllerHelper {

    private final TaskListRepository taskListRepository;

    public TaskListEntity getTaskListOrThrowException(Long id) {
        return taskListRepository
                .findById(id)
                .orElseThrow(
                        () -> new TaskListNotFound(String.format("Task list with id %s was not found", id))
                );
    }
}
