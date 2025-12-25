package com.knowledge.microservice.exceptionhandling;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * Handles EntityNotFoundException (404).
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), "NotFound");
    }

    /**
     * Handles ConstraintViolationException (validation errors).
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleValidationException(ConstraintViolationException ex) {
        Optional<ConstraintViolation<?>> violation = ex.getConstraintViolations().stream().findFirst();
        String errorMessage = violation.map(ConstraintViolation::getMessageTemplate).orElse("Validation error");

        return buildResponse(HttpStatus.BAD_REQUEST, errorMessage, "ValidationError");
    }

    /**
     * Handles MethodArgumentNotValidException (Bean Validation errors on RequestBody).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldError() != null 
                ? ex.getBindingResult().getFieldError().getDefaultMessage() 
                : "Validation error";
        return buildResponse(HttpStatus.BAD_REQUEST, errorMessage, "ValidationError");
    }

    /**
     * Handles DataIntegrityViolationException (unique constraint violations, etc.).
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityException(DataIntegrityViolationException ex) {
        String message = ex.getMessage();
        String errorMessage;

        // Simple heuristic to extract key info, might vary by DB
        if (message != null && message.contains("ConstraintViolationException")) {
             errorMessage = "Database constraint violation";
        } else {
             errorMessage = "Data integrity violation";
        }
        
        return buildResponse(HttpStatus.CONFLICT, errorMessage, "DataIntegrityViolation");
    }

    /**
     * Handles access denied (unauthorized access).
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        String errorMessage = "Unauthorized: " + ex.getMessage();
        return buildResponse(HttpStatus.UNAUTHORIZED, errorMessage, "AccessDenied");
    }


    /**
     * Catch-all handler for uncaught exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Unexpected error occurred";
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, "InternalError");
    }

    /**
     * Helper to structure consistent error responses.
     */
    private ResponseEntity<Object> buildResponse(HttpStatus status, String message, String errorType) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("error", errorType);
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }
}
