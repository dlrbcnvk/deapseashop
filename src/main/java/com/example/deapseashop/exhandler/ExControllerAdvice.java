package com.example.deapseashop.exhandler;

import com.example.deapseashop.exceptions.DuplicateEmailException;
import com.example.deapseashop.exceptions.InvalidPasswordException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    @ExceptionHandler(NullPointerException.class)
    public ErrorResult npeExHandle(NullPointerException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("NPE, NullPointerException", e.getMessage());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ErrorResult duplicateEmailExHandle(DuplicateEmailException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("DuplicateEmailException", e.getMessage());
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ErrorResult invalidPasswordExHandle(InvalidPasswordException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("InvalidPasswordException", e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("hot exception", e.getMessage());
    }
}
