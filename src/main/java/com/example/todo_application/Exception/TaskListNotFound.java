package com.example.todo_application.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskListNotFound extends RuntimeException{
    public TaskListNotFound(String message) {
        super(message);
    }
}
