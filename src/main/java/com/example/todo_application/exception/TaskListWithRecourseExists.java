package com.example.todo_application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TaskListWithRecourseExists extends RuntimeException{
    public TaskListWithRecourseExists(String message) {
        super(message);
    }
}
