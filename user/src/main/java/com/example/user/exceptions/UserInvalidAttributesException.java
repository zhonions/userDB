package com.example.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class UserInvalidAttributesException extends RuntimeException {
    public UserInvalidAttributesException(String message) {
        super(message);
    }
}
