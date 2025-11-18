package com.projects.reactivespring.exception;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReviewNotFoundException(String message) {
        super(message);
    }
}
