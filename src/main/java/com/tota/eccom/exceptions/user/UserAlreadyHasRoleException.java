package com.tota.eccom.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyHasRoleException extends RuntimeException {

    public UserAlreadyHasRoleException(String message) {
        super(message);
    }

    public UserAlreadyHasRoleException(String message, Throwable cause) {
        super(message, cause);
    }
}
