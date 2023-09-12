package com.munira.miniproject.exception;

public class BookWasNotBorrowedException extends RuntimeException {
    public BookWasNotBorrowedException(String message) {
        super(message);
    }
}
