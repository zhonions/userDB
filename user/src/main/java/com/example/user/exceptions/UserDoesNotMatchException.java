package com.example.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserDoesNotMatchException extends RuntimeException {
    public UserDoesNotMatchException(String message) {
        super(message);
    }
}
