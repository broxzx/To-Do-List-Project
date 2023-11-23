package com.example.todo_application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant dueDate;

    @NonNull
    private Boolean isDone;

}
