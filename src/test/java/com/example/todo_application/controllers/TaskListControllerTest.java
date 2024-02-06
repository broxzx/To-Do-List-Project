package com.example.todo_application.controllers;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

}
