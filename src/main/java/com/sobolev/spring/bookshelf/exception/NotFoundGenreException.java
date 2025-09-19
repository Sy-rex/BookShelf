package com.sobolev.spring.bookshelf.exception;

public class NotFoundGenreException extends RuntimeException {
    public NotFoundGenreException(String message) {
        super(message);
    }
}
