package com.koodjohvi.movieapi.exception;

public class DeletionNotAllowedException extends RuntimeException {
    public DeletionNotAllowedException(String message) {
        super(message);
    }
}
