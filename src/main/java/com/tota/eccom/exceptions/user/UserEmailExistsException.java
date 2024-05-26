package com.tota.eccom.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserEmailExistsException extends RuntimeException {
    public UserEmailExistsException(String message) {
        super(message);
    }

    public UserEmailExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
