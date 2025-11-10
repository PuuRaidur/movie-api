package com.koodjohvi.movieapi.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        String message = ex.getMessage();
        return ResponseEntity.badRequest().body(Objects.requireNonNullElse(message, "Invalid request parameters"));

        // Handle other validation errors
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = ex.getMessage().toLowerCase();
        if (message.contains("unique") || message.contains("genre_name_unique")) {
            return ResponseEntity.badRequest().body("A genre with this name already exists.");
        }
        if (message.contains("birth_date")) {
            return ResponseEntity.badRequest().body("Actor birth date is invalid.");
        }
        return ResponseEntity.badRequest().body("Database constraint violation: " + ex.getMessage());
    }

    @ExceptionHandler(DeletionNotAllowedException.class)
    public ResponseEntity<String> handleDeletionNotAllowed(DeletionNotAllowedException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleInvalidPaginationParameter(MethodArgumentTypeMismatchException ex) {
        if ("page".equals(ex.getName()) || "size".equals(ex.getName())) {
            return ResponseEntity.badRequest().body(
                    String.format("Invalid %s parameter: '%s'. Must be a positive integer.",
                            ex.getName(), ex.getValue())
            );
        }
        return ResponseEntity.badRequest().body("Invalid parameter: " + ex.getName());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnexpectedException(Exception ex) {
        // Log the error for debugging
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + ex.getMessage());
    }
}
