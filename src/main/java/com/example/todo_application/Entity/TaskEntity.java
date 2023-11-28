package com.example.todo_application.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "task_entity")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant dueDate;

    @Column(nullable = false)
    private Boolean isDone;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private TaskListEntity taskListEntity;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;

    @Override
    public String toString() {
        return "TaskEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dueDate=" + dueDate +
                ", isDone=" + isDone +
                ", taskListEntity=" + taskListEntity +
                ", createdBy=" + createdBy +
                '}';
    }
}
