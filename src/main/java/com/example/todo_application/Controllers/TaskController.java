package com.example.todo_application.Controllers;

import com.example.todo_application.Entity.TaskEntity;
import com.example.todo_application.Exception.TaskNotFoundException;
import com.example.todo_application.Exception.TaskWithRecourseExists;
import com.example.todo_application.Factory.TaskDtoFactory;
import com.example.todo_application.Repository.TaskRepository;
import com.example.todo_application.dto.TaskDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
@Transactional
public class TaskController {
    private final TaskRepository taskRepository;
    private final TaskDtoFactory taskDtoFactory;

    private static final String GET_TASKS_By_ID = "/{id}";
    private static final String UPDATE_TASK = "/{id}";
    private static final String DELETE_TASK = "/{id}";

    @Autowired
    public TaskController(TaskRepository taskRepository, TaskDtoFactory taskDtoFactory) {
        this.taskRepository = taskRepository;
        this.taskDtoFactory = taskDtoFactory;
    }

    @GetMapping()
    public List<TaskDto> getAllTasks() {
        List<TaskEntity> tasks = taskRepository.findAll();

        return tasks.stream()
                .map(taskDtoFactory::makeTaskDto)
                .collect(Collectors.toList());
    }


    @GetMapping(GET_TASKS_By_ID)
    public TaskDto getTaskDtoById(@PathVariable Long id) {
        return taskRepository.findById(id)
                .map(taskDtoFactory::makeTaskDto)
                .orElseThrow(
                        () -> new TaskNotFoundException(String.format("Task with id %s was not found", id))
                );
    }

    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody TaskEntity task) {
        if (taskRepository.existsByTitle(task.getTitle())) {
            throw new TaskWithRecourseExists(String.format("Task with title '%s' already exists", task.getTitle()));
        }

        TaskEntity createdTask = taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body("Task created with ID: " + createdTask.getId());
    }

    @PutMapping(UPDATE_TASK)
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @RequestBody TaskEntity updatedTask) {
        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(
                        () -> new TaskNotFoundException(String.format("Task with %s was not found", id))
                );

        taskEntity.setTitle(updatedTask.getTitle());
        taskEntity.setDescription(updatedTask.getDescription());
        taskEntity.setDueDate(updatedTask.getDueDate());
        taskEntity.setIsDone(updatedTask.getIsDone());

        TaskEntity updatedTaskEntity = taskRepository.save(taskEntity);
        TaskDto updatedTaskDto = taskDtoFactory.makeTaskDto(updatedTaskEntity);

        return ResponseEntity.ok(updatedTaskDto);
    }

    @DeleteMapping(DELETE_TASK)
    public void deleteTask(@PathVariable Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(String.format("Task with id %s was not found", id));
        }

        taskRepository.deleteById(id);
    }
}
