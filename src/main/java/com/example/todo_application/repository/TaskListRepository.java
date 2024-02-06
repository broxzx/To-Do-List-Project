package com.example.todo_application.repository;

import com.example.todo_application.entity.TaskListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskListRepository extends JpaRepository<TaskListEntity, Long> {
    List<TaskListEntity> findTaskListEntitiesByCreatedBy_Username(String createdBy_username);

    Optional<TaskListEntity> findTaskListEntitiesByCreatedBy_UsernameAndId(String createdBy_username, Long id);

    Optional<TaskListEntity> findByIdAndCreatedBy_Username(Long id, String createdBy_username);
}
