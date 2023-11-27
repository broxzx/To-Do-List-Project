//package com.example.todo_application.Controllers;
//
//import com.example.todo_application.Controllers.helper.ControllerHelper;
//import com.example.todo_application.Entity.TaskEntity;
//import com.example.todo_application.Entity.TaskListEntity;
//import com.example.todo_application.Factory.TaskDtoFactory;
//import com.example.todo_application.Repository.TaskRepository;
//import com.example.todo_application.dto.TaskDto;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class TaskControllerTest {
//
//    @Mock
//    private TaskRepository taskRepository;
//
//    @Mock
//    private ControllerHelper controllerHelper;
//
//    @Mock
//    private TaskDtoFactory taskDtoFactory;
//
//    @InjectMocks
//    private TaskController taskController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    @DisplayName("GET /api/{taskId}/task/ returns all tasks that TaskListEntity contains")
//    void testGetAllTasks() {
//        Long taskId = 1L;
//
//        TaskListEntity taskList = new TaskListEntity();
//        List<TaskEntity> tasks = new ArrayList<>();
//
//        tasks.add(new TaskEntity(1L, "Task 1", "Description 1", null, false, taskList));
//        tasks.add(new TaskEntity(2L, "Task 2", "Description 2", null, false, taskList));
//        taskList.setTasks(tasks);
//
//        when(controllerHelper.getTaskListOrThrowException(taskId)).thenReturn(taskList);
//        when(taskDtoFactory.makeTaskDto(any())).thenReturn(new TaskDto());
//
//        List<TaskDto> result = taskController.getAllTasks(taskId);
//
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//        assertEquals(2, result.size());
//        verify(controllerHelper, times(1)).getTaskListOrThrowException(taskId);
//        verify(taskDtoFactory, times(2)).makeTaskDto(any());
//    }
//
//    @Test
//    @DisplayName("GET /api/{taskListId}/task/{taskId} returns tasks with appropriate id that TaskListEntity contains")
//    void testGetTaskById() {
//        Long taskListId = 1L;
//        Long taskId = 2L;
//
//        TaskListEntity taskListMock = mock(TaskListEntity.class);
//        TaskEntity taskEntity = new TaskEntity(taskId, "Task 1", "Description 1", null, false, taskListMock);
//
//        when(controllerHelper.getTaskListOrThrowException(taskListId)).thenReturn(taskListMock);
//        when(taskDtoFactory.makeTaskDto(any())).thenReturn(new TaskDto());
//
//        when(taskListMock.getTasks()).thenReturn(Collections.singletonList(taskEntity));
//
//        ResponseEntity<TaskDto> result = taskController.getTaskDtoById(taskListId, taskId);
//
//        assertNotNull(result);
//        assertTrue(result.getStatusCode().is2xxSuccessful());
//        verify(controllerHelper, times(1)).getTaskListOrThrowException(taskListId);
//        verify(taskDtoFactory, times(1)).makeTaskDto(any());
//    }
//
//    @Test
//    @DisplayName("POST /api/{taskListId}/task/ creates new task in appropriate Task List")
//    void testCreateTask() {
//        Long taskListId = 1L;
//        Long taskId = 1L;
//
//        TaskListEntity taskListMock = mock(TaskListEntity.class);
//        TaskEntity taskEntity = new TaskEntity(taskId, "Task 1", "Description 1", null, false, taskListMock);
//
//        taskListMock.setTasks(List.of(taskEntity));
//
//        when(controllerHelper.getTaskListOrThrowException(taskListId)).thenReturn(taskListMock);
//        when(taskListMock.getTasks()).thenReturn(Collections.emptyList());
//        when(taskRepository.saveAndFlush(any())).thenReturn(taskEntity);
//
//        ResponseEntity<String> res = taskController.createTask(taskListId, taskEntity);
//
//        assertNotNull(res);
//        assertTrue(res.getStatusCode().is2xxSuccessful());
//        verify(controllerHelper, times(1)).getTaskListOrThrowException(taskListId);
//        verify(taskRepository, times(1)).saveAndFlush(taskEntity);
//    }
//
//    @Test
//    @DisplayName("PUT /api/{taskListId}/task/{taskId} updates existed task with id {taskId}")
//    void testUpdateTask() {
//        Long taskListId = 1L;
//        Long taskId = 1L;
//
//        TaskListEntity taskListMock = mock(TaskListEntity.class);
//        TaskEntity existedEntity = new TaskEntity(taskId, "Task 1", "Description 1", null, false, taskListMock);
//        TaskEntity updatedEntity = new TaskEntity(taskId, "Updated Task", "Updated Description", null, true, taskListMock);
//
//        when(controllerHelper.getTaskListOrThrowException(taskListId)).thenReturn(taskListMock);
//        when(taskRepository.existsById(taskId)).thenReturn(true);
//        when(taskListMock.getTasks()).thenReturn(Collections.singletonList(existedEntity));
//        when(taskRepository.saveAndFlush(any(TaskEntity.class))).thenReturn(updatedEntity);
//
//        ResponseEntity<TaskDto> result = taskController.updateTask(taskListId, taskId, updatedEntity);
//
//        assertNotNull(result);
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//        assertNotNull(result.getBody(), "Response body should not be null");
//        assertTrue(result.getStatusCode().is2xxSuccessful());
//        TaskDto updatedDto = result.getBody();
//        System.out.println("Updated Dto: " + updatedDto);
//        assertNotNull(updatedDto);
//        assertEquals("Updated Task", updatedDto.getTitle());
//        assertEquals("Updated Description", updatedDto.getDescription());
//        assertTrue(updatedDto.getIsDone());
//
//        verify(controllerHelper, times(1)).getTaskListOrThrowException(taskListId);
//        verify(taskRepository, times(1)).existsById(taskId);
//        verify(taskRepository, times(1)).saveAndFlush(any(TaskEntity.class));
//    }
//
//    @Test
//    @DisplayName("DELETE /api/task/{taskId} updates existed task with id {taskId}")
//    void testDeleteTask() {
//        Long taskId = 1L;
//
//        when(taskRepository.existsById(taskId)).thenReturn(true);
//
//        taskController.deleteTask(taskId);
//
//        verify(taskRepository, times(1)).existsById(taskId);
//        verify(taskRepository, times(1)).deleteById(taskId);
//    }
//}