package com.bank.app.rest;

import com.bank.app.exception.OperationException;
import com.bank.app.exception.ResourceNotFoundException;
import com.bank.app.model.response.ExceptionRestResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestControllerAdvice
public class ApiExceptionHandler {
    private static final String OBJECT_NOT_FOUND_EXCEPTION = "Object is not found!";
    private static final String OPERATION_EXCEPTION = "Operation exception on server side";
    private static final String VALIDATION_EXCEPTION = "Validation exception";
    private static final String OTHER_ERRORS_EXCEPTION = "Other exception on side exception!";

    @ExceptionHandler(value = ResourceNotFoundException.class)
    protected ResponseEntity<ExceptionRestResponse> handleResourceNotFoundExceptions(ResourceNotFoundException ex,
                                                                                     HttpServletRequest request) {
        log.warn("Resource Not Found Exception is occurred. Request URI = {}, Exception message {}", request.getRequestURI(), ex.getMessage());
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
        var bindingResult = ex.getBindingResult();

        Map<String, String> validationCaused = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            validationCaused.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        log.warn("Validation Exception is occurred. Request URI = {}, Exception message {}",
                request.getRequestURI(), String.join(";",
                        validationCaused.values()));


        return new ResponseEntity<>(
                new ExceptionRestResponse(request.getRequestURI(),
                        HttpStatus.BAD_REQUEST, VALIDATION_EXCEPTION,
                        validationCaused,
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = OperationException.class)
    protected ResponseEntity<ExceptionRestResponse> handleOperationExceptions(OperationException ex,
                                                                              HttpServletRequest request) {
        log.error("Operation Exception is occurred. Request URI = {}, Exception message {}", request.getRequestURI(), ex.getMessage());
        return new ResponseEntity<>(
                toExceptionRestResponse(
                        request.getRequestURI(),
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        OPERATION_EXCEPTION,
                        ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ExceptionRestResponse> handleAllOtherExceptions(HttpServletRequest request, Exception ex) {
        log.error("Exception is occurred. Request URI = {}, Exception message {}", request.getRequestURI(), ex.getMessage());
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
