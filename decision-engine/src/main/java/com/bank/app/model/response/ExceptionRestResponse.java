package com.bank.app.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ExceptionRestResponse(String path,
                                    HttpStatus statusCode,
                                    String message,
                                    String caused,
                                    Map<String, String> validationCaused,
                                    int errorCode,
                                    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                    LocalDateTime localDateTime
) {
    public ExceptionRestResponse(String path,
                                 HttpStatus statusCode,
                                 String message,
                                 String caused,
                                 int errorCode,
                                 @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                 LocalDateTime localDateTime) {
        this(path, statusCode, message, caused, null, errorCode, localDateTime);
    }

    public ExceptionRestResponse(String path,
                                 HttpStatus statusCode,
                                 String message,
                                 Map<String, String> validationCaused,
                                 int errorCode,
                                 @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                 LocalDateTime localDateTime) {
        this(path, statusCode, message, null, validationCaused, errorCode, localDateTime);
    }
}
