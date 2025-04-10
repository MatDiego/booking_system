package com.example.booking_system.handler;

import com.example.booking_system.dto.response.ErrorResponse;
import com.example.booking_system.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalRestExceptionHandler {

    private ErrorResponse createErrorResponse(HttpStatus status, String message, WebRequest request) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getDescription(false)
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(WebRequest request) {

        String userFriendlyMessage = "Invalid request body format or value provided. Please check the request " +
                "structure and data types.";

        ErrorResponse errorResponse = createErrorResponse(HttpStatus.BAD_REQUEST, userFriendlyMessage, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
