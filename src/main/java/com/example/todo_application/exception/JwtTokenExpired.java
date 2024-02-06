package com.example.todo_application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JwtTokenExpired extends RuntimeException{

    public JwtTokenExpired(String message) {
        super(message);
    }
}
