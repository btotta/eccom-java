package com.tota.eccom.exceptions.product;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ProductCategoryAlreadyExistsException extends RuntimeException {

    public ProductCategoryAlreadyExistsException(String message) {
        super(message);
    }

    public ProductCategoryAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
