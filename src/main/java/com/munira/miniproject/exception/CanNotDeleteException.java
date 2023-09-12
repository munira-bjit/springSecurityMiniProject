package com.munira.miniproject.exception;

public class CanNotDeleteException extends RuntimeException{
    public CanNotDeleteException(String message) {
        super(message);
    }
}
