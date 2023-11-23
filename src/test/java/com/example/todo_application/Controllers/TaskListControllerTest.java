package com.example.todo_application.Controllers;


import com.example.todo_application.Controllers.helper.ControllerHelper;
import com.example.todo_application.Entity.TaskListEntity;
import com.example.todo_application.Factory.TaskListDtoFactory;
import com.example.todo_application.Repository.TaskListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Method: GET '/api/'. returns task list from Data Base")
    public void getAllTaskLists() throws Exception {
        mockMvc.perform(get("/api/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("Method: POST '/api/{taskListId}/'. create new task list in Data Base")
    public void createNewTaskList() throws Exception {
        String taskListJson = "{ \"name\": \"Test Task List\", \"tasks\": [] }";

        mockMvc.perform(post("/api/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskListJson))
                .andExpect(status().isCreated())
                .andExpect(content().string("Task list with id 1 was created"));
    }

}
//    @Mock
//    private TaskListRepository taskListRepository;
//
//    @Mock
//    private ControllerHelper controllerHelper;
//
//    @Mock
//    private TaskListDtoFactory taskListDtoFactory;
//
//    @InjectMocks
//    private TaskListController taskListController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void getAllTaskList() throws Exception {
//        Long taskId = 1L;
//
//        TaskListEntity taskList1 = new TaskListEntity();
//        TaskListEntity taskList2 = new TaskListEntity();
//
//        when();

