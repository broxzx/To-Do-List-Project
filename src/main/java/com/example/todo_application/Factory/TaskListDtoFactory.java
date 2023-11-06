package com.example.todo_application.Factory;

import com.example.todo_application.Entity.TaskListEntity;
import com.example.todo_application.dto.TaskListDto;
import org.springframework.stereotype.Component;

@Component
public class TaskListDtoFactory {

    public TaskListDto makeListDto(TaskListEntity taskList) {
        return TaskListDto.builder()
                .id(taskList.getId())
                .name(taskList.getName())
                .build();
    }
}
