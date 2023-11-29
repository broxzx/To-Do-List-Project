package com.example.todo_application.Controllers;

import com.example.todo_application.Controllers.helper.ControllerHelper;
import com.example.todo_application.Entity.TaskEntity;
import com.example.todo_application.Entity.TaskListEntity;
import com.example.todo_application.Entity.UserEntity;
import com.example.todo_application.Exception.*;
import com.example.todo_application.Factory.TaskDtoFactory;
import com.example.todo_application.Repository.TaskRepository;
import com.example.todo_application.dto.TaskDto;
import com.example.todo_application.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskRepository taskRepository;

    private final ControllerHelper controllerHelper;

    private final TaskDtoFactory taskDtoFactory;

    private final UserService userService;

    private static final String GET_ALL_TASKS = "/api/{taskId}/task/";
    private static final String GET_TASK_BY_ID = "/api/{taskListId}/task/{taskId}";
    private static final String CREATE_TASK = "/api/{taskListId}/task/";
    private static final String UPDATE_TASK = "/api/{taskListId}/task/{taskId}";
    private static final String DELETE_TASK = "/api/task/{taskId}";

    private String getCurrentUsersUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }

    @PreAuthorize("isAuthenticated()")
    @PostFilter("filterObject.createdBy == principal.username or hasRole('ADMIN')")
    @GetMapping(GET_ALL_TASKS)
    public List<TaskDto> getAllTasks(@PathVariable Long taskId) {
        String currentUserName = getCurrentUsersUserName();
        TaskListEntity taskList = controllerHelper.getTaskListOrThrowException(taskId);

        if (taskList == null) {
            throw new TaskListNotFound("Task list not found");
        }

        if (taskList.getTasks().isEmpty()) {
            throw new TaskListNotFound("Task list does not contain any tasks");
        }

        if (!Objects.equals(currentUserName, taskList.getCreatedBy().getUsername())) {
            throw new UserDoesNotHavePermission("This user does not have access to task with id %s".formatted(taskId));
        }


        log.info("List Tasks have been obtained");

        return taskList
                .getTasks()
                .stream()
                .map(taskDtoFactory::makeTaskDto)
                .collect(Collectors.toList());
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping(GET_TASK_BY_ID)
    public ResponseEntity<TaskDto> getTaskDtoById(@PathVariable Long taskListId, @PathVariable Long taskId) {
        String currentUserName = getCurrentUsersUserName();
        TaskListEntity taskList = controllerHelper.getTaskListOrThrowException(taskListId);

        if (!Objects.equals(currentUserName, taskList.getCreatedBy().getUsername())) {
            throw new UserDoesNotHavePermission("This user does not have access to task with id %s".formatted(taskId));
        }

        Optional<TaskEntity> task = taskList.getTasks()
                .stream()
                .filter(anotherTask -> anotherTask.getId().equals(taskId) && anotherTask.getCreatedBy().getUsername().equals(currentUserName))
                .findFirst();

        log.info("Task with id {} was obtained", taskId);

        return task
                .map(taskEntity ->
                        ResponseEntity.ok(taskDtoFactory.makeTaskDto(taskEntity)))
                .orElseGet(() ->
                        ResponseEntity
                                .notFound()
                                .build()
                );
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(CREATE_TASK)
    public ResponseEntity<String> createTask(@PathVariable Long taskListId, @RequestBody TaskEntity task) {
        String currentUserName = getCurrentUsersUserName();
        TaskListEntity taskList = controllerHelper.getTaskListOrThrowException(taskListId);

        if (!Objects.equals(currentUserName, taskList.getCreatedBy().getUsername())) {
            throw new UserDoesNotHavePermission("This user does not have access to task with id %s".formatted(task.getId()));
        }

        UserEntity currentUserEntity = userService.findByUsername(currentUserName);
        task.setCreatedBy(currentUserEntity);

        boolean titleExists = taskList.getTasks()
                .stream()
                .anyMatch(anotherTask -> anotherTask.getTitle().equals(task.getTitle()));

        if (titleExists) {
            log.error("Task with {} title already exists", task.getTitle());
            throw new TaskWithRecourseExists(String.format("Task with %s title already exists", task.getTitle()));
        }


        taskList.addTaskToTaskList(task);

        taskRepository.save(task);

        log.info("Task has been created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Task with id %s was successfully created",task.getId()));

    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(UPDATE_TASK)
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long taskListId, @PathVariable Long taskId, @RequestBody TaskEntity task) {
        if (task.getTitle().trim().isEmpty()) {
            log.error("Task title cannot be empty");
            throw new BadRequestException("Task title cannot be empty");
        }

        String currentUserName = getCurrentUsersUserName();
        TaskListEntity taskList = controllerHelper.getTaskListOrThrowException(taskListId);

        if (!Objects.equals(currentUserName, taskList.getCreatedBy().getUsername())) {
            throw new UserDoesNotHavePermission("This user does not have access to task with id %s".formatted(task.getId()));
        }

        TaskEntity taskEntity = taskList
                .getTasks()
                .stream()
                .filter(anotherTask -> anotherTask.getId().equals(taskId))
                .findFirst()
                .orElseThrow(
                        () -> new TaskNotFoundException(String.format("Task with id %s was not found", taskId))
                );

        taskEntity.setTaskListEntity(taskList);
        taskEntity.setTitle(task.getTitle());
        taskEntity.setDescription(task.getDescription());
        taskEntity.setDueDate(task.getDueDate());
        taskEntity.setIsDone(task.getIsDone());

        taskList.updateTaskToTaskList(taskEntity);
        taskRepository.save(taskEntity);

        log.info("Task has been updated successfully");

        TaskDto updatedTaskDto = taskDtoFactory.makeTaskDto(taskEntity);
        return ResponseEntity.ok(updatedTaskDto);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(DELETE_TASK)
    public void deleteTask(@PathVariable Long taskId) {
        Optional<TaskEntity> requestedTaskEntity = taskRepository.findById(taskId);
        String currentUserName = getCurrentUsersUserName();

        if (requestedTaskEntity.isPresent()) {
            if (!requestedTaskEntity.get().getCreatedBy().getUsername().equals(currentUserName)) {
                throw new UserDoesNotHavePermission("User does not have permission to delete task with id %s".formatted(taskId));
            }
            taskRepository.deleteById(taskId);
            log.info(String.format("Task with id %s was deleted successfully", taskId));
        } else {
            log.error(String.format("Task with id %s was not found", taskId));
            throw new TaskNotFoundException(String.format("Task with id %s was not found", taskId));
        }
    }
}

