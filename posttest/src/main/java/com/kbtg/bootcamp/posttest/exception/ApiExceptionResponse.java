package com.kbtg.bootcamp.posttest.exception;

import java.time.ZonedDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiExceptionResponse {
    private final String message;
    private final ZonedDateTime dateTime;
}
