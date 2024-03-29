package com.example.todo_application.factory;

import com.example.todo_application.entity.TaskEntity;
import com.example.todo_application.dto.TaskDto;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoFactory {

    public TaskDto makeTaskDto(TaskEntity task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .isDone(task.getIsDone())
                .createdBy(task.getCreatedBy().getUsername())
                .build();
    }
}
