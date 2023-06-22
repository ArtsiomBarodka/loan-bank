package com.bank.app.rest;

import com.bank.app.exception.OperationException;
import com.bank.app.exception.ResourceNotFoundException;
import com.bank.app.model.response.ExceptionRestResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {
    private static final String DELIMITER = "; ";
    private static final String OBJECT_NOT_FOUND_EXCEPTION = "Object is not found!";
    private static final String OPERATION_EXCEPTION = "Operation exception on server side";
    private static final String VALIDATION_EXCEPTION = "Validation exception";
    private static final String OTHER_ERRORS_EXCEPTION = "Other exception on side exception!";

    @ExceptionHandler(value = ResourceNotFoundException.class)
    protected ResponseEntity<ExceptionRestResponse> handleResourceNotFoundExceptions(ResourceNotFoundException ex,
                                                                                     HttpServletRequest request) {
        return new ResponseEntity<>(
                toExceptionRestResponse(
                        request.getRequestURI(),
                        HttpStatus.NOT_FOUND,
                        OBJECT_NOT_FOUND_EXCEPTION,
                        ex.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<ExceptionRestResponse> handleValidationExceptions(MethodArgumentNotValidException ex,
                                                                                     HttpServletRequest request) {
        var message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(DELIMITER));

        return new ResponseEntity<>(
                toExceptionRestResponse(
                        request.getRequestURI(),
                        HttpStatus.BAD_REQUEST,
                        VALIDATION_EXCEPTION,
                        message),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = OperationException.class)
    protected ResponseEntity<ExceptionRestResponse> handleOperationExceptions(OperationException ex,
                                                                                     HttpServletRequest request) {
        return new ResponseEntity<>(
                toExceptionRestResponse(
                        request.getRequestURI(),
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        OPERATION_EXCEPTION,
                        ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ExceptionRestResponse> handleAllOtherExceptions(HttpServletRequest request, Exception e) {
        return new ResponseEntity<>(
                toExceptionRestResponse(
                        request.getRequestURI(),
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        OTHER_ERRORS_EXCEPTION,
                        null),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ExceptionRestResponse toExceptionRestResponse(String path,
                                                          HttpStatus statusCode,
                                                          String message,
                                                          String caused) {
        return new ExceptionRestResponse(
                path,
                statusCode,
                message,
                caused,
                statusCode.value(),
                LocalDateTime.now());
    }
}
