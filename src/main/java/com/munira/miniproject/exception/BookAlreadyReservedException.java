package com.munira.miniproject.exception;

public class BookAlreadyReservedException extends RuntimeException{
    public BookAlreadyReservedException(String message) {
        super(message);
    }
}
