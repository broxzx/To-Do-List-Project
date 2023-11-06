package com.example.todo_application.dto;

import com.example.todo_application.Entity.TaskEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskDto {
    @NonNull
    private Long id;

    @NonNull
    private String title;

    private String description;

    @NonNull
    @JsonProperty("due_date")
    private Instant dueDate;

    @NonNull
    private Boolean isDone;

}
