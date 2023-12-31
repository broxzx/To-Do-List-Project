package com.example.todo_application.Controllers;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Method: GET '/api/'. returns task list from Data Base")
    public void getAllTaskLists() throws Exception {
        mockMvc.perform(get("/api/")
                        .with(user("test"))) // use 'script.sql' in order to add user 'test' in Data Base
                .andDo(print())
                .andExpect(status().isOk())
                 .andReturn();
    }

    @Test
    @DisplayName("Method: POST '/api/{taskListId}/'. create new task list in Data Base")
    public void createNewTaskList() throws Exception {
        String taskListJson = "{ \"name\": \"Test Task List\", \"tasks\": [] }";

        mockMvc.perform(post("/api/")
                        .with(user("test"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskListJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("Task list with id 1 was created"));
    }

}
