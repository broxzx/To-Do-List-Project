package com.example.todo_application.Repository;

import com.example.todo_application.Entity.TaskListEntity;
import com.example.todo_application.dto.TaskListDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskListRepository extends JpaRepository<TaskListEntity, Long> {
    boolean existsByName(String name);
}
