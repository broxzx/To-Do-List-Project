package com.example.todo_application.Controllers;

import com.example.todo_application.Entity.TaskListEntity;
import com.example.todo_application.Entity.UserEntity;
import com.example.todo_application.Exception.TaskListNotFound;
import com.example.todo_application.Exception.TaskListWithRecourseExists;
import com.example.todo_application.Factory.TaskListDtoFactory;
import com.example.todo_application.Repository.TaskListRepository;
import com.example.todo_application.dto.TaskListDto;
import com.example.todo_application.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/api")
@Slf4j
public class TaskListController {

    private final TaskListRepository taskListRepository;

    private final TaskListDtoFactory taskListDtoFactory;


    private final UserService userService;
    private static final String CREATE_TASK_LIST = "/";
    private static final String GET_TASKS_LIST = "/";
    private static final String GET_TASK_LIST_BY_ID = "/{id}";
    private static final String UPDATE_TASK_LIST_BY_ID = "/{id}";
    private static final String DELETE_TASK_LIST_BY_ID = "/{id}";


    private String getCurrentUsersUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(GET_TASKS_LIST)
    public List<TaskListDto> getTasksList() {
        String currentUserName = getCurrentUsersUserName();

        log.info("Task List has been obtained");

        return taskListRepository.findTaskListEntitiesByCreatedBy_Username(currentUserName)
                .stream()
                .map(taskListDtoFactory::makeListDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize("isAuthenticated()")
    @PostAuthorize("returnObject.createdBy == principal.username")
    @GetMapping(GET_TASK_LIST_BY_ID)
    public TaskListDto getTaskById(@PathVariable Long id) {
        String currentUserName = getCurrentUsersUserName();

        log.info("Task List with id {} has been obtained", id);


        return taskListRepository.findTaskListEntitiesByCreatedBy_UsernameAndId(currentUserName, id)
                .map(taskListDtoFactory::makeListDto)
                .orElseThrow(
                        () -> new TaskListNotFound(String.format("Task list with id %s was not found or you do not have permission to this task", id))
                );
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping(CREATE_TASK_LIST)
    public ResponseEntity<String> createTaskList(@RequestBody TaskListEntity taskList) {
        String currentUserName = getCurrentUsersUserName();

        List<TaskListEntity> foundTaskListEntities = taskListRepository.findTaskListEntitiesByCreatedBy_Username(currentUserName);

        Optional<TaskListEntity> foundTaskListEntityWithSimilarName = foundTaskListEntities.stream()
                .filter(anotherTaskListEntity -> anotherTaskListEntity.getName().equals(taskList.getName()))
                .findAny();

        if (foundTaskListEntityWithSimilarName.isPresent()) {
            log.error("Task cannot contain name that already exists");
            throw new TaskListWithRecourseExists(String.format("Task list with name %s already exists", taskList.getName()));
        }

        UserEntity currentUserEntity = userService.findByUsername(currentUserName);

        log.info("Task has been created");

        taskList.setCreatedBy(currentUserEntity);
        TaskListEntity savedTaskListEntity = taskListRepository.save(taskList);

        return ResponseEntity.status(HttpStatus.CREATED).body(String.format("Task list with id %s was created", savedTaskListEntity.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(UPDATE_TASK_LIST_BY_ID)
    public ResponseEntity<TaskListDto> updateTaskList(@PathVariable Long id, @RequestBody TaskListEntity taskList) {
        String currentUserName = getCurrentUsersUserName();

        TaskListEntity findTaskListEntity = taskListRepository.findByIdAndCreatedBy_Username(id, currentUserName)
                .orElseThrow(
                        () -> new TaskListNotFound(String.format("Task list with id %s was not found", id))
                );

        findTaskListEntity.setName(taskList.getName());
        findTaskListEntity.setTasks(taskList.getTasks());

        TaskListEntity updatedTaskList = taskListRepository.saveAndFlush(findTaskListEntity);
        TaskListDto taskListDto = taskListDtoFactory.makeListDto(updatedTaskList);

        log.info("Task with id {} has been updated", id);

        return ResponseEntity.ok(taskListDto);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(DELETE_TASK_LIST_BY_ID)
    public ResponseEntity<String> deleteTaskListById(@PathVariable Long id) {
        String currentUserName = getCurrentUsersUserName();

        if (taskListRepository.findByIdAndCreatedBy_Username(id, currentUserName).isEmpty()) {
            log.error("Task with id {} does not exist", id);
            throw new TaskListNotFound(String.format("Task list with id %s was not found", id));
        }

        taskListRepository.deleteById(id);

        log.info("Task with id {} has been deleted", id);

        return ResponseEntity.status(HttpStatus.OK).body(String.format("Task list with id %s was deleted", id));
    }
}
