package com.example.todo_application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserDoesNotHavePermission extends RuntimeException{

    public UserDoesNotHavePermission(String message) {
        super(message);
    }
}
