package com.banquemisr.challenge05.exception;

import com.banquemisr.challenge05.exception.email.EmailServiceException;
import com.banquemisr.challenge05.exception.userException.InvalidInputException;
import com.banquemisr.challenge05.exception.userException.ResourceNotFoundException;
import com.banquemisr.challenge05.exception.userException.UserLoginException;
import com.banquemisr.challenge05.exception.userException.UserRegistrationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.logging.Level;
import java.util.logging.Logger;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        logger.warning(() -> String.format("Resource not found: %s. Request: %s", ex.getMessage(), request.getDescription(false)));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<String> handleInvalidInputException(InvalidInputException ex, WebRequest request) {
        logger.warning(() -> String.format("Invalid input: %s. Request: %s", ex.getMessage(), request.getDescription(false)));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(UserRegistrationException.class)
    public ResponseEntity<String> handleUserRegistrationException(UserRegistrationException ex, WebRequest request) {
        logger.warning(() -> String.format("User registration conflict: %s. Request: %s", ex.getMessage(), request.getDescription(false)));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(UserLoginException.class)
    public ResponseEntity<String> handleUserLoginException(UserLoginException ex, WebRequest request) {
        logger.warning(() -> String.format("User login failed: %s. Request: %s", ex.getMessage(), request.getDescription(false)));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(EmailServiceException.class)
    public ResponseEntity<String> handleEmailServiceException(EmailServiceException ex, WebRequest request) {
        logger.log(Level.SEVERE, String.format("Email service error: %s. Request: %s", ex.getMessage(), request.getDescription(false)), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex, WebRequest request) {
        logger.log(Level.SEVERE, String.format("An unexpected error occurred: %s. Request: %s", ex.getMessage(), request.getDescription(false)), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred. Please try again later.");
    }
}