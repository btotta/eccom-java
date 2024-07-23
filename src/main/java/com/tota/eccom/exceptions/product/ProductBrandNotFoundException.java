package com.tota.eccom.exceptions.product;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductBrandNotFoundException extends RuntimeException {

    public ProductBrandNotFoundException(String message) {
        super(message);
    }

    public ProductBrandNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
