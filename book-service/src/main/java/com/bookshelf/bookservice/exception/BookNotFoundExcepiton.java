package com.bookshelf.bookservice.exception;

public class BookNotFoundExcepiton extends RuntimeException {
    public BookNotFoundExcepiton(String s) {
        super(s);
    }
}
