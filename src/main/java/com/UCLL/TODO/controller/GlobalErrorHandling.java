package com.UCLL.TODO.controller;

import com.UCLL.TODO.exception.EmailAddressNotUniqueException;
import com.UCLL.TODO.exception.TodoNotFoundException;
import com.UCLL.TODO.exception.UnauthorizedException;
import com.UCLL.TODO.exception.UserNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalErrorHandling {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException e) {
        Map<String, String> map = new HashMap<>();
        map.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
    }

    @ExceptionHandler({TodoNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleTodoNotFoundException(TodoNotFoundException e) {
        Map<String, String> map = new HashMap<>();
        map.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> handleInvalidInput(MethodArgumentNotValidException e) {
        Map<String, String> map = new HashMap<>();

        e.getBindingResult().getFieldErrors()
                .forEach(error ->
                        map.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<Map<String, String>> handleInvalidJson(HttpMessageNotReadableException e) {
        Map<String, String> map = new HashMap<>();
        map.put("message", "Invalid value provided");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

    @ExceptionHandler(EmailAddressNotUniqueException.class)
    public ResponseEntity<Map<String, String>> handleEmailAddressNotUniqueException(EmailAddressNotUniqueException e) {
        Map<String, String> map = new HashMap<>();
        map.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(map);
    }

    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity<Map<String, String>> handleUnauthorizedException(UnauthorizedException e) {
        Map<String, String> map = new HashMap<>();
        map.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(map);
    }
}
