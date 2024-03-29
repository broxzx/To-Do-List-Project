package com.example.todo_application.factory;

import com.example.todo_application.entity.TaskListEntity;
import com.example.todo_application.dto.TaskListDto;
import org.springframework.stereotype.Component;

@Component
public class TaskListDtoFactory {

    public TaskListDto makeListDto(TaskListEntity taskList) {
        return TaskListDto.builder()
                .id(taskList.getId())
                .name(taskList.getName())
                .createdBy(taskList.getCreatedBy().getUsername())
                .build();
    }
}
