package com.example.todo_application.repository;

import com.example.todo_application.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    boolean existsByTitle(String title);
}
