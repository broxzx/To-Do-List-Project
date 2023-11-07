package com.example.todo_application.Controllers;

import com.example.todo_application.Entity.TaskListEntity;
import com.example.todo_application.Exception.TaskListNotFound;
import com.example.todo_application.Exception.TaskListWithRecourseExists;
import com.example.todo_application.Factory.TaskDtoFactory;
import com.example.todo_application.Factory.TaskListDtoFactory;
import com.example.todo_application.Repository.TaskListRepository;
import com.example.todo_application.dto.TaskListDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Transactional
public class TaskListController {
    private final TaskListRepository taskListRepository;
    private final TaskListDtoFactory taskListDtoFactory;

    private static final String CREATE_TASK_LIST = "/";
    private static final String GET_TASKS_LIST = "/";
    private static final String GET_TASK_LIST_BY_ID = "/{id}";
    private static final String UPDATE_TASK_LIST_BY_ID = "/{id}";
    private static final String DELETE_TASK_LIST_BY_ID = "/{id}";


    @GetMapping(GET_TASKS_LIST)
    public List<TaskListDto> getTasksList() {
        return taskListRepository.findAll()
                .stream()
                .map(taskListDtoFactory::makeListDto)
                .collect(Collectors.toList());
    }

    @GetMapping(GET_TASK_LIST_BY_ID)
    public TaskListDto getTaskById(@PathVariable Long id) {
        return taskListRepository.findById(id)
                .map(taskListDtoFactory::makeListDto)
                .orElseThrow(
                        () -> new TaskListNotFound(String.format("Task list with id %s was not found", id))
                );
    }

    @PostMapping(CREATE_TASK_LIST)
    public ResponseEntity<String> createTaskList(@RequestBody TaskListEntity taskList) {
        if (taskListRepository.existsByName(taskList.getName())) {
            throw new TaskListWithRecourseExists(String.format("Task list with name %s already exists", taskList.getName()));
        }

        TaskListEntity savedTaskListEntity = taskListRepository.save(taskList);
        return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Task list with id %s was created", savedTaskListEntity.getId()));
    }

    @PutMapping(UPDATE_TASK_LIST_BY_ID)
    public ResponseEntity<TaskListDto> updateTaskList(@PathVariable Long id, @RequestBody TaskListEntity taskList) {
        TaskListEntity foundTaskListEntity = taskListRepository.findById(id)
                .orElseThrow(
                        () -> new TaskListNotFound(String.format("Task list with id %s was not found", id))
                );

        foundTaskListEntity.setName(taskList.getName());
        foundTaskListEntity.setTasks(taskList.getTasks());

        TaskListEntity updatedTaskList = taskListRepository.save(foundTaskListEntity);
        TaskListDto taskListDto = taskListDtoFactory.makeListDto(updatedTaskList);

        return ResponseEntity.ok(taskListDto);
    }

    @DeleteMapping(DELETE_TASK_LIST_BY_ID)
    public ResponseEntity<String> deleteTaskListById(@PathVariable Long id) {
        if (taskListRepository.findById(id).isEmpty()) {
            throw new TaskListNotFound(String.format("Task list with id %s was not found", id));
        }

        taskListRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(String.format("Task list with id %s was deleted", id));
    }
}
