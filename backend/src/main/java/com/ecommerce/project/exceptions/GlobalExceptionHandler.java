package com.ecommerce.project.exceptions;

import com.ecommerce.project.payload.ApiResponse;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String,String> response = new HashMap<>();
            ex.getBindingResult().getFieldErrors().forEach(error -> response.put(error.getField(), error.getDefaultMessage()));
            ApiResponse<Map<String, String>> apiResponse = new ApiResponse<>("Validation failed", response);
        return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiResponse<String> apiResponse = new ApiResponse<>(ex.getMessage(),null);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExist.class)
    public ResponseEntity<ApiResponse<String>> handleResourceAlreadyExist(ResourceAlreadyExist ex) {
        ApiResponse<String> apiResponse = new ApiResponse<>(ex.getMessage(),null);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(APIException.class)
    public ResponseEntity<ApiResponse<String>> handleAPIException(APIException ex) {
        ApiResponse<String> apiResponse = new ApiResponse<>(ex.getMessage(),null);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }
}
