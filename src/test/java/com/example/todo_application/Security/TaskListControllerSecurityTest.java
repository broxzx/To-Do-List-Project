package com.example.todo_application.Security;

import com.example.todo_application.Entity.TaskListEntity;
import com.example.todo_application.Repository.TaskListRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskListControllerSecurityTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private TaskListRepository taskListRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testUserAccessToTaskList_GetTasksList_expectedStatus200() throws Exception {
        this.mockMvc.perform(get("/api/")
                        .with(user("user")))
                .andExpect(status().isOk());
    }

    @Test
    void testGuestAccessToTaskList_expectedStatus302() throws Exception {
        this.mockMvc.perform(get("/api/"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testAccessToLoginPage_expectedStatus200() throws Exception {
        this.mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

//    @Test
//    void testUserAccessToTaskList_GetTasksListById_expectedStatus200() throws Exception {
//        TaskListEntity taskList = new TaskListEntity(1L, "Dummy Task List", null, null);
//
//        when(taskListRepository.findById(1L)).thenReturn(Optional.of(taskList));
//
//
//        this.mockMvc.perform(get("/api/1")
//                        .with(user("user")))
//                .andExpect(status().isOk());
//    }

    @Test
    void testGuestAccessToTaskList_GetTasksListById_expectedStatus302() throws Exception {
        TaskListEntity taskList = new TaskListEntity(1L, "Dummy Task List", null, null);

        when(taskListRepository.findById(1L)).thenReturn(Optional.of(taskList));

        this.mockMvc.perform(get("/api/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testUserAccessToTaskList_CreateTaskList_expectedStatus200()  {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("user", "password");

        ResponseEntity<String> responseEntity = restTemplate
                .withBasicAuth("user", "password")
                .getForEntity("/api/", String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testUserAccessToTaskList_CreateTaskList_expectedStatus302() throws Exception {
        this.mockMvc.perform(post("/api/"))
                .andExpect(status().is3xxRedirection());
    }

//    @Test
//    void testUserAccessToTaskList_UpdateTaskList_expectedStatus200() throws Exception {
//        TaskListEntity existedTaskList = new TaskListEntity(1L, "New Task List", null, null);
//        TaskListEntity updatedTaskList = new TaskListEntity(1L, "Updated Task List", null, null);
//
//        when(taskListRepository.findById(1L)).thenReturn(Optional.of(existedTaskList));
//        when(taskListRepository.saveAndFlush(updatedTaskList)).thenReturn(updatedTaskList);
//
//        this.mockMvc.perform(put("/api/{id}", 1L)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updatedTaskList))
//                        .with(user("test")))
//                .andExpect(status().isOk());
//    }

    @Test
    void testUserAccessToTaskList_UpdateTaskList_expectedStatus302() throws Exception {
        TaskListEntity taskList = new TaskListEntity(1L, "Dummy Task List", null, null);

        when(taskListRepository.findById(1L)).thenReturn(Optional.of(taskList));

        this.mockMvc.perform(get("/api/1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testUserAccessToTaskList_DeleteTaskList_expectedStatus200() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("user", "password");

        ResponseEntity<String> responseEntity = restTemplate
                .withBasicAuth("user", "password")
                .getForEntity("/api/1", String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testUserAccessToTaskList_DeleteTaskList_expectedStatus302() throws Exception {
        this.mockMvc.perform(
                delete("/api/1"))
                .andExpect(status().is3xxRedirection());
    }
}
