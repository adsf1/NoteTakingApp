package org.example.notetakingapp.errorhandling;

import org.example.notetakingapp.errorhandling.exceptions.NoTitleException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoTitleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNoTitleException(NoTitleException ex){
        return ex.getMessage();
    }
}
