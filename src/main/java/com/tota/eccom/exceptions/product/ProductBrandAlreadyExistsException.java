package com.tota.eccom.exceptions.product;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ProductBrandAlreadyExistsException extends RuntimeException {

    public ProductBrandAlreadyExistsException(String message) {
        super(message);
    }

    public ProductBrandAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
