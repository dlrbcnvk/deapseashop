package com.example.deapseashop.exceptions;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException(String msg) {
        super(msg);
    }
}
