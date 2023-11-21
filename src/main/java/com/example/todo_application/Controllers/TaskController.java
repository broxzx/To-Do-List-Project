package com.example.todo_application.Controllers;

import com.example.todo_application.Controllers.helper.ControllerHelper;
import com.example.todo_application.Entity.TaskEntity;
import com.example.todo_application.Entity.TaskListEntity;
import com.example.todo_application.Exception.BadRequestException;
import com.example.todo_application.Exception.TaskListNotFound;
import com.example.todo_application.Exception.TaskNotFoundException;
import com.example.todo_application.Exception.TaskWithRecourseExists;
import com.example.todo_application.Factory.TaskDtoFactory;
import com.example.todo_application.Repository.TaskRepository;
import com.example.todo_application.dto.TaskDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    private static final String GET_ALL_TASKS = "/api/{taskId}/task/";
    private static final String GET_TASK_BY_ID = "api/{taskListId}/task/{taskId}";
    private static final String CREATE_TASK = "/api/{taskListId}/task/";
    private static final String UPDATE_TASK = "/api/{taskListId}/task/{taskId}";
    private static final String DELETE_TASK = "api/task/{taskId}";

    @GetMapping(GET_ALL_TASKS)
    public List<TaskDto> getAllTasks(@PathVariable Long taskId) {
        TaskListEntity taskList = controllerHelper.getTaskListOrThrowException(taskId);

        if (taskList == null) {
            throw new TaskListNotFound("Task list not found");
        }

        if (taskList.getTasks().isEmpty()) {
            throw new TaskListNotFound("Task list does not contain any tasks");
        }

        log.info("List Tasks have been obtained");

        return taskList
                .getTasks()
                .stream()
                .map(taskDtoFactory::makeTaskDto)
                .collect(Collectors.toList());
    }


    @GetMapping(GET_TASK_BY_ID)
    public ResponseEntity<TaskDto> getTaskDtoById(@PathVariable Long taskListId, @PathVariable Long taskId) {
        TaskListEntity taskList = controllerHelper.getTaskListOrThrowException(taskListId);

        Optional<TaskEntity> task = taskList.getTasks()
                .stream()
                .filter(anotherTask -> anotherTask.getId().equals(taskId))
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

    @PostMapping(CREATE_TASK)
    public ResponseEntity<String> createTask(@PathVariable Long taskListId, @RequestBody TaskEntity task) {
        TaskListEntity taskList = controllerHelper.getTaskListOrThrowException(taskListId);

        boolean titleExists = taskList.getTasks()
                .stream()
                .anyMatch(anotherTask -> anotherTask.getTitle().equals(task.getTitle()));

        if (titleExists) {
            log.error("Task with {} title already exists", task.getTitle());
            throw new TaskWithRecourseExists(String.format("Task with %s title already exists", task.getTitle()));
        }

        taskList.addTaskToTaskList(task);

        taskRepository.saveAndFlush(task);

        log.info("Task has been created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Task with id %s was successfully created",task.getId()));

    }

    @PutMapping(UPDATE_TASK)
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long taskListId, @PathVariable Long taskId, @RequestBody TaskEntity task) {
        if (task.getTitle().trim().isEmpty()) {
            log.error("Task title cannot be empty");
            throw new BadRequestException("Task title cannot be empty");
        }

        TaskListEntity taskList = controllerHelper.getTaskListOrThrowException(taskListId);

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
        taskRepository.saveAndFlush(taskEntity);

        log.info("Task has been updated successfully");

        return ResponseEntity.ok(taskDtoFactory.makeTaskDto(taskEntity));
    }

    @DeleteMapping(DELETE_TASK)
    public void deleteTask(@PathVariable Long taskId) {
        boolean exists = taskRepository.existsById(taskId);

        if (exists) {
            log.error("Task does with id {} not exist", taskId);
            taskRepository.deleteById(taskId);

        } else {
            log.info("Task has been deleted");
            throw new TaskNotFoundException("task was not found");
        }
    }
}

