package com.example.todo_application.dto;

import com.example.todo_application.Entity.TaskListEntity;
import lombok.*;

import java.util.List;

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
