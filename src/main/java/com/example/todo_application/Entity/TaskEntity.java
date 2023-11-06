package com.example.todo_application.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "task_entity")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    private String description;

    private Instant dueDate;

    @Column(nullable = false)
    private Boolean isDone;

    @ManyToOne()
    @JoinColumn(name = "task_id")
    private TaskListEntity taskListEntity;

    public TaskEntity(String title, String description, Instant dueDate, Boolean isDone) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isDone = isDone;
    }
}
