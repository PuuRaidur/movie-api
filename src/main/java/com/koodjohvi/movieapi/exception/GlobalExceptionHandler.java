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

    @ExceptionHandler(DeletionNotAllowedException.class)
    public ResponseEntity<String> handleDeletionNotAllowed(DeletionNotAllowedException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // ✅ Handle validation errors (null fields, invalid formats, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    // ✅ Handle type mismatch errors (string instead of number, invalid date format)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String fieldName = ex.getName();
        String invalidValue = ex.getValue() != null ? ex.getValue().toString() : "null";
        String targetType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";

        String message = String.format("Invalid value '%s' for field '%s'. Expected type: %s",
                invalidValue, fieldName, targetType);

        return ResponseEntity.badRequest().body(message);
    }

    // ✅ Handle database constraint violations (duplicate genre names, etc.)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = ex.getMessage().toLowerCase();

        if (message.contains("unique") || message.contains("genre_name_unique")) {
            return ResponseEntity.badRequest().body("A genre with this name already exists");
        }

        if (message.contains("birth_date") || message.contains("date")) {
            return ResponseEntity.badRequest().body("Invalid birth date format. Use YYYY-MM-DD");
        }

        if (message.contains("genre")) {
            return ResponseEntity.badRequest().body("Database constraint violation for genre");
        }

        if (message.contains("actor")) {
            return ResponseEntity.badRequest().body("Database constraint violation for actor");
        }

        return ResponseEntity.badRequest().body("Database constraint violation: " + ex.getMessage());
    }

    // ✅ Handle all other IllegalArgumentExceptions (pagination, etc.)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    // ✅ Catch-all for unexpected errors (should rarely happen with proper validation)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnexpectedException(Exception ex) {
        // Log the error for debugging
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred. Please try again later.");
    }
}
