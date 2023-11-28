package com.example.todo_application.dto;

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

    @NonNull
    private String createdBy;
}
