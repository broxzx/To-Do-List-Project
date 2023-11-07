package com.example.todo_application.Entity;

import com.example.todo_application.Exception.TaskNotFoundException;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "task_list_entity")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class TaskListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taskListEntity")
    private List<TaskEntity> tasks;

    public void addTaskToTaskList(TaskEntity taskEntity) {
        tasks.add(taskEntity);
        taskEntity.setTaskListEntity(this);
    }

    public void updateTaskToTaskList(TaskEntity task) {
        tasks.stream()
                .filter(anotherTask -> anotherTask.getId().equals(task.getId()))
                .findFirst()
                .ifPresent(taskEntity -> {
                    taskEntity.setTitle(task.getTitle());
                    taskEntity.setDescription(task.getDescription());
                    taskEntity.setDueDate(task.getDueDate());
                    taskEntity.setIsDone(task.getIsDone());
                });
    }

    public void deleteTaskFromTaskList(TaskEntity task) {
         TaskEntity taskEntity = tasks.stream()
                .filter(anotherTask -> anotherTask.getId().equals(task.getId()))
                .findFirst()
                .orElseThrow(
                        () -> new TaskNotFoundException(String.format("Task with %s id was not found", task.getId()))
                );
    }
}
