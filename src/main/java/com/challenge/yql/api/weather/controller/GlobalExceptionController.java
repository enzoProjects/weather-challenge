package com.challenge.yql.api.weather.controller;

import com.challenge.yql.api.weather.service.exception.UserCreationException;
import com.challenge.yql.api.weather.service.exception.YqlClientException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by springfield-home on 7/9/17.
 */
@ControllerAdvice
@Component
public class GlobalExceptionController {

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map handle(MethodArgumentNotValidException exception) {
        return error(exception.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList()));
    }


    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map handle(ConstraintViolationException exception) {
        return error(exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList()));
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map handle(UserCreationException exception) {
        return error(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map handle(YqlClientException exception) {
        return error(exception.getMessage());
    }



    private Map error(Object message) {
        return Collections.singletonMap("error", message);
    }
}