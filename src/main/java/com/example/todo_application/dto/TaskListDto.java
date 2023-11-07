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

}
