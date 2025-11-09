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

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        String message = ex.getMessage();
        if (message != null && (
                message.contains("Page number cannot be negative") ||
                        message.contains("Page size must be at least 1") ||
                        message.contains("Page size cannot exceed")
        )) {
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.badRequest().body(ex.getMessage());
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
        if (ex.getMessage().contains("genre_name_unique")) {
            return ResponseEntity.badRequest().body("Genre with this name already exists");
        }
        if (ex.getMessage().contains("actor_birth_date_check")) {
            return ResponseEntity.badRequest().body("Invalid birth date format");
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
                    String.format("Invalid %s parameter: %s", ex.getName(), ex.getValue())
            );
        }
        return ResponseEntity.badRequest().body("Invalid parameter: " + ex.getName());
    }
}
