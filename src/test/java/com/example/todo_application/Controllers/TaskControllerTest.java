package com.example.todo_application.Controllers;

import com.example.todo_application.Controllers.TaskController;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskControllerTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ControllerHelper controllerHelper;

    @Mock
    private TaskDtoFactory taskDtoFactory;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("GET /api/{taskId}/task/ returns all tasks that TaskListEntity contains")
    void testGetAllTasks() {
        Long taskId = 1L;
        TaskListEntity taskList = new TaskListEntity();
        List<TaskEntity> tasks = new ArrayList<>();
        tasks.add(new TaskEntity(1L, "Task 1", "Description 1", null, false, taskList));
        tasks.add(new TaskEntity(2L, "Task 2", "Description 2", null, false, taskList));
        taskList.setTasks(tasks);

        when(controllerHelper.getTaskListOrThrowException(taskId)).thenReturn(taskList);
        when(taskDtoFactory.makeTaskDto(any())).thenReturn(new TaskDto());

        List<TaskDto> result = taskController.getAllTasks(taskId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
        verify(controllerHelper, times(1)).getTaskListOrThrowException(taskId);
        verify(taskDtoFactory, times(2)).makeTaskDto(any());
    }

    @Test
    void testGetTaskDtoById() {

    }

    @Test
    void testCreateTask() {
    }

    @Test
    void testUpdateTask() {
    }

    @Test
    void testDeleteTask() {
    }
}