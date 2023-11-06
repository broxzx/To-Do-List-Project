package com.example.todo_application.dto;

import com.example.todo_application.Entity.TaskListEntity;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskListDto {
    @NonNull
    private Long id;

    @NonNull
    private String name;

    public TaskListDto convertTaskListEntityToTaskListDto(TaskListEntity taskList) {
        return TaskListDto.builder()
                .id(taskList.getId())
                .name(taskList.getName())
                .build();
    }

}
