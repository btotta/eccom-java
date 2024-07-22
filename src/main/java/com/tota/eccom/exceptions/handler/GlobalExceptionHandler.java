package com.tota.eccom.exceptions.handler;

import com.tota.eccom.adapters.dto.exception.ErrorDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleAllExceptions(Exception ex, WebRequest request) {
        HttpStatus httpStatus = getHttpStatus(ex);
        return buildErrorResponse(ex, request, httpStatus);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<ErrorDetails> handleIllegalArgumentExceptions(IllegalArgumentException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<ErrorDetails> handleAccessDeniedExceptions(AccessDeniedException ex, WebRequest request) {
        return buildErrorResponse(ex, request, HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<ErrorDetails> buildErrorResponse(Exception ex, WebRequest request, HttpStatus httpStatus) {
        ErrorDetails errorDetails = ErrorDetails.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .details(request.getDescription(false).replace("uri=", ""))
                .build();

        log.error("Error: {}", errorDetails);

        return new ResponseEntity<>(errorDetails, httpStatus);
    }

    private HttpStatus getHttpStatus(Exception ex) {
        ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);
        return (responseStatus != null) ? responseStatus.value() : HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
