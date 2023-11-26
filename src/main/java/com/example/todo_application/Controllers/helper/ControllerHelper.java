package com.example.todo_application.Controllers.helper;

import com.example.todo_application.Entity.TaskListEntity;
import com.example.todo_application.Exception.TaskListNotFound;
import com.example.todo_application.Repository.TaskListRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

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
