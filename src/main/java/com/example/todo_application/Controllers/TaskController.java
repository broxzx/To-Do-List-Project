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

        if (taskList.getTasks().isEmpty()) {
            throw new TaskListNotFound("Task list does not contain any tasks");
        }

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
            throw new TaskWithRecourseExists(String.format("Task with %s title already exists", task.getTitle()));
        }

        taskList.addTaskToTaskList(task);

        taskRepository.save(task);

        return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Task with id %s was successfully created",task.getId()));

    }

    @PutMapping(UPDATE_TASK)
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long taskListId, @PathVariable Long taskId, @RequestBody TaskEntity task) {
        if (task.getTitle().trim().isEmpty()) {
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
        taskRepository.save(taskEntity);

        return ResponseEntity.ok(taskDtoFactory.makeTaskDto(taskEntity));
    }

    @DeleteMapping(DELETE_TASK)
    public void deleteTask(@PathVariable Long taskId) {
//        TaskListEntity taskList = controllerHelper.getTaskListOrThrowException(taskListId);
//
//        TaskEntity taskEntity = taskList.getTasks()
//                .stream()
//                .filter(anotherTask -> anotherTask.getId().equals(taskId))
//                .findFirst()
//                .orElseThrow(
//                        () -> new TaskNotFoundException(String.format("Task with id %s was not found", taskId))
//                );
        boolean exists = taskRepository.existsById(taskId);

        if (exists) {
            taskRepository.deleteById(taskId);

        } else {
            throw new TaskNotFoundException("task was not found");
        }
//        taskList.deleteTaskFromTaskList(taskEntity);
    }
}
