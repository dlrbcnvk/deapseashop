package com.example.deapseashop.exceptions;

public class DuplicateEmailAndUsernameException extends RuntimeException {
    public DuplicateEmailAndUsernameException(String msg) {
        super(msg);
    }
}
