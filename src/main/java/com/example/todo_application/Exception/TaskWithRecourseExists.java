package com.example.todo_application.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TaskWithRecourseExists extends RuntimeException {
    public TaskWithRecourseExists(String message) {
        super(message);
    }
}
