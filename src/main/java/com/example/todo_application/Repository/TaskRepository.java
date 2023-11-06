package com.example.todo_application.Repository;

import com.example.todo_application.Entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    boolean existsByTitle(String title);
}
