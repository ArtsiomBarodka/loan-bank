package com.bank.app.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ExceptionRestResponse(String path,
                                    HttpStatus statusCode,
                                    String message,
                                    String caused,
                                    int errorCode,
                                    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                    LocalDateTime localDateTime
) {
}
